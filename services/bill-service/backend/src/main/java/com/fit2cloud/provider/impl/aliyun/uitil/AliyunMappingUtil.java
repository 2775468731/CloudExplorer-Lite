package com.fit2cloud.provider.impl.aliyun.uitil;

import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponseBody;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.constants.BillModeConstants;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  5:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunMappingUtil {

    /**
     * 将阿里云账单对象转化为系统账单对象
     *
     * @param item            阿里云账单对象
     * @param syncBillRequest 获取阿里云账单请求对象
     * @param regions         区域
     * @return 系统账单对象
     */
    public static CloudBill toCloudBill(DescribeInstanceBillResponseBody.DescribeInstanceBillResponseBodyDataItems item, SyncBillRequest syncBillRequest, List<Credential.Region> regions) {
        CloudBill cloudBill = new CloudBill();
        cloudBill.setId(UUID.randomUUID().toString().replace("-", ""));
        cloudBill.setBillMode(toBillMode(item.getSubscriptionType()));
        cloudBill.setRegionName(item.getRegion());
        regions.stream().filter(region -> region.getName().equals(item.getRegion())).findFirst().ifPresent(region -> cloudBill.setRegionId(region.getRegionId()));
        cloudBill.setZone(item.getZone());
        cloudBill.setTags(toTagsMap(item.getTag()));
        cloudBill.setProjectId(item.getCostUnit());
        cloudBill.setProductName(item.getProductName());
        cloudBill.setResourceId(item.getInstanceID());
        cloudBill.setProjectName(item.getCostUnit());
        cloudBill.setProductId(item.getProductCode());
        cloudBill.setProductDetail(item.getProductDetail());
        cloudBill.setResourceName(item.getNickName());
        cloudBill.setProvider(PlatformConstants.fit2cloud_ali_platform.name());
        if (StringUtils.isNotEmpty(item.getBillingDate())) {
            cloudBill.setUsageStartDate(CommonUtil.getLocalDateTime(item.getBillingDate(), "yyyy-MM-dd"));
            cloudBill.setBillingCycle(CommonUtil.getLocalDateTime(item.getBillingDate(), "yyyy-MM-dd"));
            cloudBill.setUsageEndDate(CommonUtil.getLocalDateTime(item.getBillingDate(), "yyyy-MM-dd"));
        } else {
            cloudBill.setUsageStartDate(CommonUtil.getLocalDateTime(syncBillRequest.getBillingCycle(), "yyyy-MM"));
            cloudBill.setBillingCycle(CommonUtil.getLocalDateTime(syncBillRequest.getBillingCycle(), "yyyy-MM"));
            cloudBill.setUsageEndDate(CommonUtil.getLocalDateTime(syncBillRequest.getBillingCycle(), "yyyy-MM"));
        }
        cloudBill.setPayAccountId(item.getBillAccountID());
        cloudBill.setTotalCost(BigDecimal.valueOf(item.getPretaxGrossAmount()));
        cloudBill.setRealTotalCost(BigDecimal.valueOf(item.getCashAmount()));
        return cloudBill;
    }

    /**
     * 订阅类型，取值：
     * <p>
     * Subscription：预付费。
     * PayAsYouGo：后付费。
     *
     * @param subscriptionType 阿里云计费模式
     * @return 云管计费模式
     */
    private static String toBillMode(String subscriptionType) {
        if (subscriptionType.equals("Subscription")) {
            return BillModeConstants.MONTHLY.name();
        } else if (subscriptionType.equals("PayAsYouGo")) {
            return BillModeConstants.ON_DEMAND.name();
        } else {
            return BillModeConstants.OTHER.name();
        }
    }

    /**
     * 获取标签map
     *
     * @param tags 标签
     * @return 标签对象
     */
    private static Map<String, Object> toTagsMap(String tags) {
        //  key:testKey value:testValue; key:testKey1 value:testValues1
        if (StringUtils.isNotEmpty(tags)) {
            String[] split = tags.split(";");
            return Arrays.stream(split).flatMap(item -> {
                String[] s = item.split(" ");
                HashMap<String, Object> paramsTags = new HashMap<>();
                if (s.length == 1) {
                    String key = s[0].replace("key:", "").trim();
                    paramsTags.put(key, "");
                }
                if (s.length == 2) {
                    String key = s[0].replace("key:", "").trim();
                    String value = s[1].replace("value:", "").trim();
                    paramsTags.put(key, value);
                }
                return paramsTags.entrySet().stream();
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            return new HashMap<>();
        }
    }

}
