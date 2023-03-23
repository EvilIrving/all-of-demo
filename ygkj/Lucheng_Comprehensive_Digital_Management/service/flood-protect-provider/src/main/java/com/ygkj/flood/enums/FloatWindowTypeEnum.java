package com.ygkj.flood.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FloatWindowTypeEnum {

    // 1大中型水库，2流域水位，3平原水位
    RESERVIOR("1", "大中型水库", "RR"), BASIN("2", "流域水位", "ZZ,DD"), PLAIN("3", "平原水位", "ZZ,DD");

    private String type;

    private String stScale;

    private String stType;

    public static String getValue(String type) {
        FloatWindowTypeEnum[] typeEnums = values();
        for (FloatWindowTypeEnum typeEnum : typeEnums) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum.getStType();
            }
        }
        return null;
    }

    public static String getScale(String type) {
        FloatWindowTypeEnum[] typeEnums = values();
        for (FloatWindowTypeEnum typeEnum : typeEnums) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum.getStScale();
            }
        }
        return null;
    }
}
