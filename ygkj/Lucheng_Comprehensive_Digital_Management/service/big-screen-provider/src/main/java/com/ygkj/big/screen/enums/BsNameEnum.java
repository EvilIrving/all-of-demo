package com.ygkj.big.screen.enums;

/**
 * @author xq
 * @Description
 * @Date 2021/6/28
 */
public enum BsNameEnum {
    WATER_DISASTER_PREVENT("水灾害防御"),
    WATER_RESOURCE_GUARANTEE("水资源保障"),
    RIVER_LAKE_RSVR_PROTECT("河湖库保护"),
    WATER_DEVELOP_PLAN("水发展规划"),
    WATER_EVENT_MONITOR("水事务监管"),
    WATER_GOVERN_COOPERATE("水政务协同"),
    COMPOSITE_MAP("水利数据资产");
    String bsName;

    BsNameEnum(String bsName) {
        this.bsName = bsName;
    }

    public String getBsName() {
        return bsName;
    }
}
