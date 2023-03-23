package com.ygkj.gragh.enums;

public enum EquipmentType implements Type {
    RAINFALL("雨情"),
    WATER_LEVEL("水情"),
    GATE("工情"),
    CAMERA("视频");

    private String type;

    EquipmentType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
