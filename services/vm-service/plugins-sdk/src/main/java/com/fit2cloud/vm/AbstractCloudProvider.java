package com.fit2cloud.vm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.vm.entity.*;
import com.fit2cloud.vm.entity.result.CheckCreateServerResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:35 AM
 * @Version 1.0
 * @注释:
 */
public abstract class AbstractCloudProvider<C extends Credential> implements ICloudProvider {
    public static class NotSupportException extends RuntimeException {

    }

    @Override
    public FormObject getCreateServerForm() {
        throw new RuntimeException("Form does not exist");
    }

    /**
     * 获取认证信息
     *
     * @param credential 认证字符串
     * @return 认证对象
     */
    protected C getCredential(String credential) {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        try {
            return new ObjectMapper().readValue(credential, new ObjectMapper().constructType(trueType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取认证信息
     *
     * @param req 请求对象字符串
     * @return 认证对象
     */
    protected C getCredentialByRequest(String req) {
        ObjectNode jsonNodes = JsonUtil.parseObject(req);
        JsonNode credential = jsonNodes.get("credential");
        if (credential != null) {
            return getCredential(credential.asText());
        }
        throw new Fit2cloudException(1001, "不存在认证对象");
    }

    @Override
    public List<F2CHost> listHost(String req) {
        throw new NotSupportException();
    }

    @Override
    public List<F2CDatastore> listDataStore(String req) {
        throw new NotSupportException();
    }

    @Override
    public boolean detachDisk(String req) {
        return false;
    }

    @Override
    public boolean deleteDisk(String req) {
        return false;
    }

    @Override
    public boolean enlargeDisk(String req) {
        return false;
    }

    @Override
    public F2CDisk attachDisk(String req) {
        return new F2CDisk();
    }

    @Override
    public List<F2CDisk> createDisks(String req) {
        return null;
    }

    @Override
    public F2CDisk createDisk(String req) {
        return null;
    }

    @Override
    public FormObject getCreateDiskForm() {
        return null;
    }

    @Override
    public List<Map<String, String>> getDiskTypes(String req) {
        return null;
    }

    @Override
    public List<Map<String, String>> getDeleteWithInstance(String req) {
        return null;
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req) {
        return List.of();
    }

    @Override
    public F2CVirtualMachine getSimpleServerByCreateRequest(String req) {
        return null;
    }

    @Override
    public F2CVirtualMachine createVirtualMachine(String req) {
        return null;
    }

    @Override
    public CheckCreateServerResult validateServerCreateRequest(String req) {
        return CheckCreateServerResult.success();
    }

    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return null;
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return null;
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return null;
    }

    @Override
    public boolean powerOff(String req) {
        return false;
    }

    @Override
    public boolean powerOn(String req) {
        return false;
    }

    @Override
    public boolean shutdownInstance(String req) {
        return false;
    }

    @Override
    public boolean rebootInstance(String req) {
        return false;
    }

    @Override
    public boolean deleteInstance(String req) {
        return false;
    }

    @Override
    public boolean hardShutdownInstance(String req) {
        return false;
    }

    @Override
    public boolean hardRebootInstance(String req) {
        return false;
    }

    @Override
    public String calculateConfigPrice(String req) {
        return "";
    }

    /**
     * 宿主机监控信息
     *
     * @param req
     * @return
     */
    @Override
    public List<F2CPerfMetricMonitorData> getF2CHostPerfMetricMonitorData(String req) {
        return new ArrayList<>();
    }

    /**
     * 云磁盘监控信息
     *
     * @param req
     * @return
     */
    @Override
    public List<F2CPerfMetricMonitorData> getF2CDiskPerfMetricMonitorData(String req) {
        return new ArrayList<>();
    }

    /**
     * 存储器监控信息
     *
     * @param req
     * @return
     */
    @Override
    public List<F2CPerfMetricMonitorData> getF2CDatastorePerfMetricMonitorData(String req) {
        return new ArrayList<>();
    }

    @Override
    public F2CVirtualMachine changeVmConfig(String req) {
        return null;
    }

    @Override
    public FormObject getConfigUpdateForm() {
        return null;
    }

    @Override
    public String calculateConfigUpdatePrice(String req) {
        return null;
    }

    @Override
    public List<F2CDisk> getVmF2CDisks(String req) {
        return new ArrayList<>();
    }

    @Override
    public boolean supportResetPassword() {
        return false;
    }

    @Override
    public boolean resetPassword(String req) {
        return false;
    }
}
