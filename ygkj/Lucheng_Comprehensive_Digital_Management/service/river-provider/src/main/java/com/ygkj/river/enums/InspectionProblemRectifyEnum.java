package com.ygkj.river.enums;

/**
 * @author lxl
 * @create 2021-08-06 20:48
 * @description 蓝线整改问题 整改状态
 */
public enum InspectionProblemRectifyEnum {


    NO_RECTIFY("无需整改",0),
    NOT_RECTIFY("未整改",1),
    RECTIFY("已整改",2),
    OUT_TIME("逾期未整改",3),
    ABNORMAL("异常",4);


    private Integer value;

    private String key;


    public Integer getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    InspectionProblemRectifyEnum(String key, Integer value) {
        this.value = value;
        this.key = key;
    }
}
