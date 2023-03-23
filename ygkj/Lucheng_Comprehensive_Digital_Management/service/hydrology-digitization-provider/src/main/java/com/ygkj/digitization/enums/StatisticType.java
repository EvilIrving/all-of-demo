package com.ygkj.digitization.enums;

public enum StatisticType implements Type{
    RAINFALL("雨量"),
    WATER_LEVEL("水位"),
    WATER_QUALITY("水质"),
    FLOW("流量");

    private String type;

    StatisticType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
