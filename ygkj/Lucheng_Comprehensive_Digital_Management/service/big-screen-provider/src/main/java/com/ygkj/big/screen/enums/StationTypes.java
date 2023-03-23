package com.ygkj.big.screen.enums;

import com.ygkj.enums.AbstractStationType;

/**
 * @author fml
 * @date 2021-03-23 10:10
 * @description 测站信息
 */
public enum StationTypes implements AbstractStationType {

    RAINFALL("PP","st_pptn_r_","雨量站"),
    RESERVOIR("RR","st_rsvr_r_","水库水位站"),
    RESERVOIR2("R2","st_rsvr_r_","水库水位站"),
    RESERVOIR3("R3","st_rsvr_r_","水库水位站"),
    RIVER("ZZ","st_river_r_","河道水位站"),
    GATE("DD","st_was_r_","堰闸工情站"),
    TIDE("TT","st_tide_r_","潮位站");

    StationTypes(String type, String table) {
        this(type, table, new String());
    }

    StationTypes(String type, String table, String description) {
        this.type = type;
        this.table = table;
        this.description = description;
    }

    String type;

    String table;

    String description;


    @Override
    public String type() {
        return type;
    }

    @Override
    public String table() {
        return table;
    }
}
