package com.ygkj.enums;

import com.ygkj.utils.StringUtils;

/**
 * @author xq
 * @Description
 * @Date 2021/5/12
 */
public enum EngScalEnum {
    L1("大（Ⅰ）型", "1", 1),
    L2("大（Ⅱ）型", "2", 2),
    MID("中型", "3", 3),
    S1("小（Ⅰ）型", "4", 4),
    S2("小（Ⅱ）型", "5", 5),
    LARGE_AND_MID("大中型", "1,2,3", -1),
    SMALL("小型", "3,4", -1);
    String desc;
    String engScal;
    int engScalAsInt;

    EngScalEnum(String desc, String engScal, int engScalAsInt) {
        this.desc = desc;
        this.engScal = engScal;
        this.engScalAsInt = engScalAsInt;
    }

    public String getDesc() {
        return desc;
    }

    public String getEngScal() {
        return engScal;
    }

    public int getEngScalAsInt() {
        if (engScal.contains(StringUtils.JOIN_DELIMITER)) {
            return -1;
        } else {
            return engScalAsInt;
        }
    }

    public static EngScalEnum getByDesc(String desc) {
        if (desc != null && !desc.trim().equals("")) {

        } else {
            return null;
        }
        switch (desc) {
            case "大（Ⅰ）型":
                return L1;
            case "大（Ⅱ）型":
                return L2;
            case "中型":
                return MID;
            case "小（Ⅰ）型":
                return S1;
            case "小（Ⅱ）型":
                return S2;
            case "大中型":
                return LARGE_AND_MID;
            case "小型":
                return SMALL;
            default:
                return null;
        }
    }

    public static EngScalEnum getByLevel(String engScal) {
        if (engScal != null && engScal.trim() != "") {

        } else {
            return null;
        }
        switch (engScal) {
            case "1":
                return L1;
            case "2":
                return L2;
            case "3":
                return MID;
            case "4":
                return S1;
            case "5":
                return S2;
            default:
                return null;
        }
    }
}
