package com.ygkj.big.screen.enums;

import com.ygkj.enums.Type;

public enum MessageTypes implements Type {
    HEAVY_RAIN("暴雨"),
    WATER_LEVEL("水库"),
    MOUNTAIN_FLOOD("山洪");

    private String type;

    MessageTypes(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }

    public static MessageTypes candidateType(String type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case "暴雨":
                return HEAVY_RAIN;
            case "水库":
                return WATER_LEVEL;
            case "山洪":
                return MOUNTAIN_FLOOD;
            default:
                break;
        }
        return null;
    }
}
