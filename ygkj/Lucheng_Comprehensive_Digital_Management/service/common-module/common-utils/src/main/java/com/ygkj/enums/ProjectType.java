package com.ygkj.enums;

public enum ProjectType implements Type {

    RESERVOIR("水库"),
    HYDROPOWER("水电站"),
    PUMP("泵站"),
    SLUICE("水闸"),
    DIKE("堤防"),
    SEAWALL("海塘"),
    POOL("山塘"),
    RIVER("河道"),
    IRR("灌区"),
    GATE("闸站"),
    CWS("农村供水工程"),
    OTHER("其他");

    private String type;

    ProjectType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
