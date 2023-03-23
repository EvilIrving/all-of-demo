package com.ygkj.gragh.enums;

import com.ygkj.enums.Type;

public enum RainfallSurfaceType implements Type {
    SURFACE("面雨量"),
    POINT("点雨量");

    private String type;

    RainfallSurfaceType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
