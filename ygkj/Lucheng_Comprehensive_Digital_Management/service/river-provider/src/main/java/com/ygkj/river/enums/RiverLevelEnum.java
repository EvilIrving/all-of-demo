package com.ygkj.river.enums;

/**
 * @author xq
 * @Description
 * @Date 2021/8/8
 */
public enum RiverLevelEnum {
    Nation(1, "国控"),
    Province(2, "省级"),
    City(3, "市级"),
    County(4, "县级"),
    Town(5, "镇级"),
    Village(6, "村级");
    Integer type;
    String desc;

    RiverLevelEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static RiverLevelEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case 1:
                return Nation;
            case 2:
                return Province;
            case 3:
                return City;
            case 4:
                return County;
            case 5:
                return Town;
            case 6:
                return Village;
            default:
                return null;
        }
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
