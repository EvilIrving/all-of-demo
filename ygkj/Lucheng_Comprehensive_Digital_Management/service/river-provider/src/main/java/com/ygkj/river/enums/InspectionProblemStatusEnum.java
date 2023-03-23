package com.ygkj.river.enums;

/**
 * @author lxl
 * @create 2021-08-06 20:54
 * @description 蓝线整改问题 状态枚举
 */
public enum InspectionProblemStatusEnum {


    NORMAL("正常", 1),
    AB_NORMAL("不正常", 2);

    private Integer value;

    private String key;


    public Integer getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    InspectionProblemStatusEnum(String key, Integer value) {
        this.value = value;
        this.key = key;
    }
}
