package com.fit2cloud.provider.impl.vsphere.entity.request;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.entity.Bill;
import com.fit2cloud.provider.impl.vsphere.VsphereBillProvider;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/9  16:46}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class VsphereBill implements Bill {
    @Form(inputType = InputType.Radio, defaultValue = "api", execModule = "finance-management", textField = "key", valueField = "value", method = "getSyncModes", clazz = VsphereBillProvider.class, attrs = "{\"style\":\"width:100%\"}")
    private String syncMode;

    @Override
    public void verification() {
        if (StringUtils.isEmpty(syncMode)) {
            throw new Fit2cloudException(1001, "useBucket为必填参数");
        }
    }

    @Override
    public Map<String, Object> getDefaultParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("syncMode", "api");
        return params;
    }
}
