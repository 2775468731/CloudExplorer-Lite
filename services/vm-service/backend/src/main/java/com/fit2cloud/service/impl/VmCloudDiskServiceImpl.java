package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseVmCloudServerService;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.controller.request.CreateJobRecordRequest;
import com.fit2cloud.controller.request.ExecProviderMethodRequest;
import com.fit2cloud.controller.request.ResourceState;
import com.fit2cloud.controller.request.disk.ListVmRequest;
import com.fit2cloud.controller.request.disk.PageVmCloudDiskRequest;
import com.fit2cloud.dao.mapper.VmCloudDiskMapper;
import com.fit2cloud.dao.mapper.VmCloudServerMapper;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.impl.vsphere.util.ResourceConstants;
import com.fit2cloud.service.IResourceOperateService;
import com.fit2cloud.service.IVmCloudDiskService;
import com.fit2cloud.service.OrganizationCommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class VmCloudDiskServiceImpl extends ServiceImpl<BaseVmCloudDiskMapper, VmCloudDisk> implements IVmCloudDiskService {
    @Resource
    private OrganizationCommonService organizationCommonService;
    @Resource
    private VmCloudDiskMapper diskMapper;
    @Resource
    private VmCloudServerMapper serverMapper;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private IBaseVmCloudServerService vmCloudServerService;
    @Resource
    private IResourceOperateService resourceOperateService;

    @Override
    public IPage<VmCloudDiskDTO> pageVmCloudDisk(PageVmCloudDiskRequest request) {
        // 普通用户
        if (CurrentUserUtils.isUser()) {
            request.setWorkspaceId(CurrentUserUtils.getWorkspaceId());
        }
        // 组织管理员
        if (CurrentUserUtils.isOrgAdmin()) {
            request.setOrganizationId(CurrentUserUtils.getOrganizationId());
            request.setOrganizationIds(organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId()));
        }
        // 构建查询参数
        QueryWrapper<VmCloudDiskDTO> wrapper = addQuery(request);
        Page<VmCloudDiskDTO> page = new Page<>(request.getCurrentPage(), request.getPageSize(), true);
        IPage<VmCloudDiskDTO> result = diskMapper.pageList(page, wrapper);
        return result;
    }

    private QueryWrapper<VmCloudDiskDTO> addQuery(PageVmCloudDiskRequest request) {
        QueryWrapper<VmCloudDiskDTO> wrapper = new QueryWrapper<>();
        //排序
        if (request.getOrder() != null && StringUtils.isNotEmpty(request.getOrder().getColumn())) {
            wrapper.orderBy(true, request.getOrder().isAsc(), ColumnNameUtil.getColumnName(request.getOrder().getColumn(), VmCloudDiskDTO.class));
        } else {
            wrapper.orderBy(true, false, "vm_cloud_disk.update_time");
        }
        wrapper.like(StringUtils.isNotBlank(request.getWorkspaceId()), "vm_cloud_disk.workspace_id", request.getWorkspaceId());
        //wrapper.in(CollectionUtils.isNotEmpty(request.getOrganizationIds()),"vm_cloud_disk.organization_id",request.getOrganizationIds());
        wrapper.like(StringUtils.isNotBlank(request.getDiskName()), "vm_cloud_disk.disk_name", request.getDiskName());
        wrapper.like(StringUtils.isNotBlank(request.getAccountName()), "cloud_account.name", request.getAccountName());
        wrapper.like(StringUtils.isNotBlank(request.getVmInstanceName()), "vm_cloud_server.instance_name", request.getVmInstanceName());
        return wrapper;
    }

    public List<VmCloudServerDTO> cloudServerList(ListVmRequest req) {
        return serverMapper.selectListByAccountId(req.getAccountId(),req.getZone());
    }

    public VmCloudDiskDTO cloudDisk(String id) {
        return diskMapper.selectDiskDetailById(id);
    }

    public boolean enlarge(String id, long newDiskSize) {
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);

            // 扩容前状态
            VmCloudDisk beforeAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeAttach);

            // 扩容中状态
            vmCloudDisk.setStatus(F2CDiskStatus.ENLARGING);
            VmCloudDisk processingAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingAttach);

            // 扩容后状态
            vmCloudDisk.setStatus(beforeAttach.getStatus());
            vmCloudDisk.setSize(newDiskSize);
            VmCloudDisk afterAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterAttach);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("newDiskSize", newDiskSize);
            params.put("instanceUuid", vmCloudDisk.getInstanceUuid());

            // 执行
            ResourceState resourceState = ResourceState.builder().beforeResource(beforeAttach).processingResource(processingAttach).afterResource(afterAttach).build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder().execMethod(ICloudProvider::enlargeDisk).methodParams(params).platform(platform).build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder().resourceOperateType(OperatedTypeEnum.ENLARGE_DISK).resourceId(id).resourceType(ResourceTypeEnum.CLOUD_DISK).jobType(JobTypeConstants.CLOUD_DISK_OPERATE_JOB).build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState, this::updateCloudDisk);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to enlarge cloud disk: " + e.getMessage());
        }
    }

    public boolean attach(String id, String instanceUuid, Boolean deleteWithInstance) {
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);
            if (!isInstanceExist(instanceUuid)) {
                throw new RuntimeException("Virtual Machine is not exist.");
            }

            // 挂载前状态
            VmCloudDisk beforeAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeAttach);

            // 挂载中状态
            vmCloudDisk.setStatus(F2CDiskStatus.ATTACHING);
            VmCloudDisk processingAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingAttach);

            // 挂载后状态
            vmCloudDisk.setStatus(F2CDiskStatus.IN_USE);
            vmCloudDisk.setInstanceUuid(instanceUuid);
            vmCloudDisk.setDeleteWithInstance(DeleteWithInstance.NO.name());
            if (deleteWithInstance) {
                vmCloudDisk.setDeleteWithInstance(DeleteWithInstance.YES.name());
            }
            VmCloudDisk afterAttach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterAttach);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("instanceUuid", instanceUuid);
            params.put("deleteWithInstance", DeleteWithInstance.NO.name());
            if (deleteWithInstance) {
                params.put("deleteWithInstance", DeleteWithInstance.YES.name());
            }

            // 执行
            ResourceState resourceState = ResourceState.builder().beforeResource(beforeAttach).processingResource(processingAttach).afterResource(afterAttach).build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder().execMethod(ICloudProvider::attachDisk).methodParams(params).platform(platform).build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder().resourceOperateType(OperatedTypeEnum.ATTACH_DISK).resourceId(id).resourceType(ResourceTypeEnum.CLOUD_DISK).jobType(JobTypeConstants.CLOUD_DISK_OPERATE_JOB).build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState, this::updateCloudDisk);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to attach cloud disk: " + e.getMessage());
        }
    }

    public boolean detach(String id) {
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);
            if (!isInstanceExist(vmCloudDisk.getInstanceUuid())) {
                throw new RuntimeException("Virtual Machine is not exist.");
            }

            // 卸载前状态
            VmCloudDisk beforeDetach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeDetach);

            // 卸载中状态
            vmCloudDisk.setStatus(F2CDiskStatus.DETACHING);
            VmCloudDisk processingDetach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingDetach);

            // 卸载后状态
            vmCloudDisk.setStatus(F2CDiskStatus.AVAILABLE);
            vmCloudDisk.setInstanceUuid(ResourceConstants.NO_INSTANCE);
            vmCloudDisk.setDevice("");
            vmCloudDisk.setDeleteWithInstance("");
            VmCloudDisk afterDetach = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterDetach);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());
            params.put("instanceUuid", beforeDetach.getInstanceUuid());

            // 执行
            ResourceState resourceState = ResourceState.builder().beforeResource(beforeDetach).processingResource(processingDetach).afterResource(afterDetach).build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder().execMethod(ICloudProvider::detachDisk).methodParams(params).platform(platform).build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder().resourceOperateType(OperatedTypeEnum.DETACH_DISK).resourceId(id).resourceType(ResourceTypeEnum.CLOUD_DISK).jobType(JobTypeConstants.CLOUD_DISK_OPERATE_JOB).build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState, this::updateCloudDisk);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to detach cloud disk!" + e.getMessage(), e);
        }
    }

    public boolean delete(String id) {
        try {
            VmCloudDisk vmCloudDisk = baseMapper.selectById(id);

            // 删除前状态
            VmCloudDisk beforeDelete = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, beforeDelete);

            // 删除中状态
            vmCloudDisk.setStatus(F2CDiskStatus.DELETING);
            VmCloudDisk processingDelete = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, processingDelete);

            // 删除后状态
            vmCloudDisk.setStatus(F2CDiskStatus.DELETED);
            VmCloudDisk afterDelete = new VmCloudDisk();
            BeanUtils.copyProperties(vmCloudDisk, afterDelete);

            // 构建执行插件方法的参数
            CloudAccount cloudAccount = cloudAccountService.getById(vmCloudDisk.getAccountId());
            String platform = cloudAccount.getPlatform();
            HashMap<String, Object> params = CommonUtil.getParams(cloudAccount.getCredential(), vmCloudDisk.getRegion());
            params.put("diskId", vmCloudDisk.getDiskId());

            // 执行
            ResourceState resourceState = ResourceState.builder().beforeResource(beforeDelete).processingResource(processingDelete).afterResource(afterDelete).build();
            ExecProviderMethodRequest execProviderMethod = ExecProviderMethodRequest.builder().execMethod(ICloudProvider::deleteDisk).methodParams(params).platform(platform).build();
            CreateJobRecordRequest createJobRecordRequest = CreateJobRecordRequest.builder().resourceOperateType(OperatedTypeEnum.DELETE_DISK).resourceId(id).resourceType(ResourceTypeEnum.CLOUD_DISK).jobType(JobTypeConstants.CLOUD_DISK_OPERATE_JOB).build();
            resourceOperateService.operateWithJobRecord(createJobRecordRequest, execProviderMethod, resourceState, this::updateCloudDisk);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete cloud disk!" + e.getMessage(), e);
        }
    }

    public boolean batchAttach(String[] ids, String instanceUuid, Boolean deleteWithInstance) {
        for (String id : ids) {
            attach(id, instanceUuid, deleteWithInstance);
        }
        return true;
    }

    public boolean batchDetach(String[] ids) {
        for (String id : ids) {
            detach(id);
        }
        return true;
    }

    public boolean batchDelete(String[] ids) {
        for (String id : ids) {
            delete(id);
        }
        return true;
    }

    /**
     * 更新磁盘
     *
     * @param vmCloudDisk
     */
    private void updateCloudDisk(VmCloudDisk vmCloudDisk) {
        baseMapper.updateById(vmCloudDisk);
    }

    /**
     * 判断虚拟机是否存在
     *
     * @param instanceUuid
     * @return
     */
    private boolean isInstanceExist(String instanceUuid) {
        Boolean result = true;
        QueryWrapper<VmCloudServer> wrapper = new QueryWrapper<VmCloudServer>()
                .eq(ColumnNameUtil.getColumnName(VmCloudServer::getInstanceUuid, true), instanceUuid);
        VmCloudServer vmCloudServer = vmCloudServerService.getOne(wrapper);

        if (vmCloudServer == null) {
            result = false;
        }
        return result;
    }
}
