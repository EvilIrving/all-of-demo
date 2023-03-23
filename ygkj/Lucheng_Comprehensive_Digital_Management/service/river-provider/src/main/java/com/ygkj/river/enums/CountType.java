package com.ygkj.river.enums;

import com.ygkj.enums.AbstractStationType;

public enum CountType implements AbstractStationType {
    RIVER("att_river_base", "河流"),
    LAKE("att_lk_base", "湖泊"),
    RIVER_SCENE("river_union_info", "河景");

    CountType(String table, String type) {
        this.table = table;
        this.type = type;
    }

    String table;

    String type;

    @Override
    public String table() {
        return table;
    }

    @Override
    public String type() {
        return type;
    }
}
