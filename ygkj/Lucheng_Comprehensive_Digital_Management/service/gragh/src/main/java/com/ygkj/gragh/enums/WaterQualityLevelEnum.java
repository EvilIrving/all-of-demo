package com.ygkj.gragh.enums;

/**
 * @author xq
 * @Description
 * @Date 2021/8/5
 */
public enum WaterQualityLevelEnum {
    CLASS_1("Ⅰ类", 1, "wql1"),
    CLASS_2("Ⅱ类", 2, "wql2"),
    CLASS_3("Ⅲ类", 3, "wql3"),
    CLASS_4("Ⅳ类", 4, "wql4"),
    CLASS_5("Ⅴ类", 5, "wql5"),
    CLASS_6("劣Ⅴ类", 6, "wql6");

    String desc;

    Integer type;

    String code;

    WaterQualityLevelEnum(String desc, Integer type, String code) {
        this.desc = desc;
        this.type = type;
        this.code = code;
    }

    public Integer type() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public static WaterQualityLevelEnum valueOfType(Integer type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case 1:
                return CLASS_1;
            case 2:
                return CLASS_2;
            case 3:
                return CLASS_3;
            case 4:
                return CLASS_4;
            case 5:
                return CLASS_5;
            case 6:
                return CLASS_6;
            default:
                return null;
        }
    }

}
