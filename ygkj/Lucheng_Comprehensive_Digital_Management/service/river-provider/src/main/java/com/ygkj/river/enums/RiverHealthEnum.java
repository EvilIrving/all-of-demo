package com.ygkj.river.enums;

/**
 * @author xq
 * @Description
 * @Date 2021/8/8
 */
public enum RiverHealthEnum {
    Red(3, "红码"), Yellow(2, "黄码"), Green(1, "绿码");
    Integer type;
    String desc;

    RiverHealthEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static RiverHealthEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case 1:
                return Green;
            case 2:
                return Yellow;
            case 3:
                return Red;
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
