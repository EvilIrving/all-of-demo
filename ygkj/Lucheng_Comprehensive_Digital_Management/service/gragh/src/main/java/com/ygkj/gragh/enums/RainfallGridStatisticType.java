package com.ygkj.gragh.enums;

import com.ygkj.enums.Type;

public enum RainfallGridStatisticType implements Type {
    ADMINISTRATIVE_DiVISIONS("行政区划"),
    BASIN("流域");

    private String statisticType;

    RainfallGridStatisticType(String statisticType) {
        this.statisticType = statisticType;
    }

    @Override
    public String type() {
        return statisticType;
    }
}
