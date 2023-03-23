package com.ygkj.visualization.enums;

import com.ygkj.enums.Type;

public enum WeatherQueryApi implements Type {
    SEVEN_DAY_WEATHER_PREDICT("1","七天天气预报接口","http://www.wz121.com/chartweb/countData?stationId=58659&type=十天预报"),
    TWENTY_FOUR_WEATHER_PREDICT("2","二十四小时气象接口","http://www.wz121.com/chartweb/changeData");

    WeatherQueryApi(String type, String description, String url) {
        this.type = type;
        this.description = description;
        this.url = url;
    }

    private String type;

    private String description;

    private String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String type() {
        return type;
    }
}
