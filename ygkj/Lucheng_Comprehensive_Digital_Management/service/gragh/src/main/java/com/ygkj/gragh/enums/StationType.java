package com.ygkj.gragh.enums;

public enum StationType implements AbstractStationType {

    RAINFALL("PP", "st_pptn_r_", "雨量站"),
    RESERVOIR("RR", "st_rsvr_r_", "水库水位站"),
    RESERVOIR2("R2", "st_rsvr_r_", "水库水位站"),
    RESERVOIR3("R3", "st_rsvr_r_", "水库水位站"),
    RIVER("ZZ", "st_river_r_", "河道水位站"),
    GATE("DD", "st_was_r_", "堰闸工情站"),
    TIDE("TT", "st_tide_r_", "潮汐水位站"),
    PUST("PT", "st_pust_r", "泵站工情");

    StationType(String type, String table) {
        this(type, table, new String());
    }

    StationType(String type, String table, String description) {
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
