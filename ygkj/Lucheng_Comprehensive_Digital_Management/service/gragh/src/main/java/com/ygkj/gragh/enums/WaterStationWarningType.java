package com.ygkj.gragh.enums;

public enum WaterStationWarningType implements WarningType {

    SUPER_WARNING("超警戒"),
    SUPER_LIMIT("超汛限"),
    SUPER_GUARANTEE("超保证"),
    NORMAL_RIVER("一般河道"),
    NORMAL_RESERVOIR("一般水库");

    String type;

    WaterStationWarningType(String type) {
        this.type = type;
    }


    @Override
    public String type() {
        return type;
    }
}
