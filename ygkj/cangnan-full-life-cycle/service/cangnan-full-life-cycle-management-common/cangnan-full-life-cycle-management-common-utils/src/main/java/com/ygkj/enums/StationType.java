package com.ygkj.enums;

import java.util.Calendar;
import java.util.Date;

public enum  StationType implements AbstractStationType {

    RAINFALL("PP","st_pptn_r_","雨量站"),
    RESERVOIR("RR","st_rsvr_r_","水库水位站"),
    RESERVOIR2("R2","st_rsvr_r_","水库水位站"),
    RESERVOIR3("R3","st_rsvr_r_","水库水位站"),
    RIVER("ZZ","st_river_r_","河道水位站"),
    GATE("DD","st_was_r_","堰闸工情站"),
    TIDE("TT","st_tide_r_","潮位站"),
    SOIL("SS","st_soli_r","墒情站"),;

    StationType(String type, String table){
        this(type,table,new String());
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

    public String getDescription(){
        return description;
    }

    public static StationType match(String type) {
        for (StationType stationType : StationType.values()) {
            if (stationType.type().equalsIgnoreCase(type)) {
                return stationType;
            }
        }
        return null;
    }

    public static String candidateTable(String type){
        StationType stationType = match(type);
        if (null != stationType){
            String table = stationType.table();
            if (table.endsWith("_")){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                int monthIndex = calendar.get(Calendar.MONTH) + 1;
                String month;
                if (monthIndex < 10){
                    month = "0"+monthIndex;
                } else {
                    month = String.valueOf(monthIndex);
                }
                table = table.concat(year.concat(month));
            }
            return table;
        }
        return null;
    }
}
