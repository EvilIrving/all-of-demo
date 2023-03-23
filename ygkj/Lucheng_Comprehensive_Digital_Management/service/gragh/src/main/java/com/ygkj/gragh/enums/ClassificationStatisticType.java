package com.ygkj.gragh.enums;

import com.ygkj.enums.Type;

public enum ClassificationStatisticType implements Type {
    TOTAL("全部"),
    ONLINE("在线"),
    OFFLINE("离线");

    private String type;

    ClassificationStatisticType(String type) {
        this.type = type;
    }


    @Override
    public String type() {
        return type;
    }
}
