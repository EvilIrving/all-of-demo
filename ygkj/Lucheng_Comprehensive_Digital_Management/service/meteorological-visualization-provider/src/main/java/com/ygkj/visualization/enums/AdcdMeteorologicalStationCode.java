package com.ygkj.visualization.enums;

import com.ygkj.enums.Type;

public enum AdcdMeteorologicalStationCode implements Type {
    WENZHOU("温州市","58659")
//    ,
//    YONGJIA("永嘉县", "58658"),
//    YUEQING("乐清市", "58656"),
//    RUIAN("瑞安市", "58752"),
//    WENCHENG("文成县", "58750"),
//    PINGYANG("平阳县", "58751"),
//    TAISHUN("泰顺县", "58746"),
//    CANGNAN("苍南县", "58755"),
//    DONGTOU("洞头区", "58760")
    ;


    String type;

    String stationCode;

    AdcdMeteorologicalStationCode(String type, String stationCode) {
        this.type = type;
        this.stationCode = stationCode;
    }

    @Override
    public String type() {
        return type;
    }

    public String getStationCode() {
        return stationCode;
    }
}
