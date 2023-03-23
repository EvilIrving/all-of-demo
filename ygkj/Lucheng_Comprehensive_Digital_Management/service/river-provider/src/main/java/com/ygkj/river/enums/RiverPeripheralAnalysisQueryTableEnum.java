package com.ygkj.river.enums;

import com.ygkj.enums.Type;

public enum RiverPeripheralAnalysisQueryTableEnum implements Type {
    OUTLET("入河排污口", "att_river_outlet_base", "id"),
    CWS("农饮水", "att_cws_base", "cws_code"),
    WIU("取水口", "att_wain_mon_base", "wain_code"),
    BUILDING_PROJECT("在建工程", "att_project", "project_id"),
    DIKE("堤防", "att_dike_base", "dike_code"),
    MOUNTAIN_POOL("山塘", "att_mnpd_base", "mnpd_code"),
    RESERVOIR("水库", "att_res_base", "res_code"),
    HYDROPOWER_STATION("水电站", "att_hyst_base", "hyst_code"),
    WAGA("水闸", "att_waga_base", "waga_code"),
    PUMP("泵站", "att_pust_base", "pust_code"),
    SEAWALL("海塘", "att_seawall_base", "seawall_code"),
    CAMERA("视频", "hikvision_camera_record", "camera_index_code"),
    GULLY("雨水口", "att_riv_inlet_base", "exp_no"),
    RIVER_SCENE("河道河景", "river_union_info", "id");


    private String type;

    private String table;

    private String primary;

    RiverPeripheralAnalysisQueryTableEnum(String type, String table, String primary) {
        this.type = type;
        this.table = table;
        this.primary = primary;
    }


    @Override
    public String type() {
        return type;
    }

    public String getTable() {
        return table;
    }

    public String getPrimary() {
        return primary;
    }

    public static RiverPeripheralAnalysisQueryTableEnum judge(String type) {
        switch (type) {
            case "入河排污口":
                return OUTLET;
            case "农饮水":
                return CWS;
            case "取水口":
                return WIU;
            case "在建工程":
                return BUILDING_PROJECT;
            case "堤防":
                return DIKE;
            case "山塘":
                return MOUNTAIN_POOL;
            case "水库":
                return RESERVOIR;
            case "水电站":
                return HYDROPOWER_STATION;
            case "水闸":
                return WAGA;
            case "泵站":
                return PUMP;
            case "海塘":
                return SEAWALL;
            case "视频":
                return CAMERA;
            case "雨水口":
                return GULLY;
            case "河道河景":
                return RIVER_SCENE;
            default:
                return null;
        }
    }
}
