package com.fit2cloud.common.platform.bill.impl;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.platform.bill.Bill;
import com.fit2cloud.common.provider.impl.aliyun.AliyunBaseCloudProvider;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/11  11:05 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class AliBill implements Bill {

    @Form(inputType = InputType.SwitchBtn, label = "使用桶", defaultValue = "false", defaultJsonValue = true)
    private Boolean useBucket;

    @Form(inputType = InputType.SingleSelect, label = "区域", relationShows = {"useBucket"}, textField = "name", valueField = "regionId", method = "getRegions", relationTrigger = "useBucket", clazz = AliyunBaseCloudProvider.class)
    private String regionId;

    @Form(inputType = InputType.SingleSelect, label = "桶", relationShows = "regionId", textField = "name", valueField = "name", relationTrigger = "regionId", method = "getBuckets", clazz = AliyunBaseCloudProvider.class)
    private String bucketId;

    @Override
    public void verification() {
        if (useBucket == null) {
            throw new Fit2cloudException(1001, "useBucket为必填参数");
        }
        if (useBucket) {
            if (StringUtils.isEmpty(regionId) || StringUtils.isEmpty(bucketId)) {
                throw new Fit2cloudException(1002, "获取数据使用桶后, 区域id和桶id为必填参数");
            }
        }
    }
}
