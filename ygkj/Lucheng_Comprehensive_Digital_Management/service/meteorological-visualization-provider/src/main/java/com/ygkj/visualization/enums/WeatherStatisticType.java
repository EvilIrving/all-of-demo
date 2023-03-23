package com.ygkj.visualization.enums;

import com.ygkj.enums.Type;

public enum WeatherStatisticType implements Type {
    TEMPERATURE("温度","temp"),
    RAINFALL("降雨量","rainfall"),
    WIND("风速","windSpeed");

    WeatherStatisticType(String type, String column) {
        this.type = type;
        this.column = column;
    }

    private String type;

    private String column;


    public String getColumn() {
        return column;
    }

    @Override
    public String type() {
        return type;
    }
}
