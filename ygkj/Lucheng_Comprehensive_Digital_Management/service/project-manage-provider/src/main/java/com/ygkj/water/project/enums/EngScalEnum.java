package com.ygkj.water.project.enums;

import org.junit.platform.commons.util.StringUtils;

/**
 * @author xq
 * @Description 工程规模枚举
 * @Date 2021/2/3
 */
public enum EngScalEnum {
    L1("大（1）型", 1),
    L2("大（2）型", 2),
    MID("中型", 3),
    S1("小（1）型", 4),
    S2("小（2）型", 5);
    String desc;
    //    String level;
    int engScal;

    EngScalEnum(String desc, int engScal) {
        this.desc = desc;
//        this.level = level;
        this.engScal = engScal;
    }

    public String getDesc() {
        return desc;
    }

    public String getLevel() {
        return String.valueOf(engScal);
    }

    public int getEngScal() {
        return engScal;
    }

    public static EngScalEnum getByDesc(String desc) {
        if (StringUtils.isBlank(desc)) {
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
            default:
                return null;
        }
    }

    public static EngScalEnum getByLevel(String level) {
        if (StringUtils.isBlank(level)) {
            return null;
        }
        switch (level) {
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
