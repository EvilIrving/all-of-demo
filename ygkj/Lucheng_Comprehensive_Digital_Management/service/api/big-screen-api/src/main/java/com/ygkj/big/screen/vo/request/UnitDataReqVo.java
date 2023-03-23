package com.ygkj.big.screen.vo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/7/12
 */
@Data
public class UnitDataReqVo implements Serializable {

    public final Integer REALTIME_DATA_TYPE = 1;

    public final Integer DRILL_DATA_TYPE = 0;

    private static final long serialVersionUID = 8717574105185553735L;
    String caseId;
    String unitName;
    String dataApi;
    String dataParam;
    Date timestamp;
    Integer dataType;

    String strategy;

    Class strategyClass;

    LinkedHashMap<String, Object> dataParamMap;

    public void putParam(String key, Object value) {
        if (dataParamMap == null) {
            dataParamMap = new LinkedHashMap<>();
        }
        dataParamMap.put(key, value);
    }

    public Object getParam(String key) {
        return getParam(key, Object.class);
    }

    public Object getParam(String key, Object defaultValue) {
        return getParam(key, Object.class, defaultValue);
    }

    public <T> T getParam(String key, Class<T> tClass) {
        return getParam(key, tClass, null);
    }

    public <T> T getParam(String key, Class<T> tClass, T defaultValue) {
        Object result = null;
        if (dataParamMap != null && (result = dataParamMap.get(key)) != null) {
            return tClass.cast(result);
        } else {
            return defaultValue;
        }
    }


}
