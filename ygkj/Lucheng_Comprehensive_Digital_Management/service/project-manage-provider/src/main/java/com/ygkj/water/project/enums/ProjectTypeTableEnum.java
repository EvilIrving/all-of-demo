package com.ygkj.water.project.enums;

import lombok.Getter;

/**
 * @author lxl
 * @create 2021-07-22 19:38
 * @description
 */
@Getter
public enum ProjectTypeTableEnum {


    RES("水库", "att_res_base"),
    PUST("泵站", "att_pust_base"),
    DIKE("堤防", "att_dike_base"),
    MNPD("山塘", "att_mnpd_base"),
    WAGA("水闸", "att_waga_base"),
    SEAWALL("海塘", "att_seawall_base"),
    HYST("水电站", "att_hyst_base"),
    GATE("闸站", "att_gate_base"),
    IRR("灌区", "att_irr_base"),
    BZD("避灾点", "avoidance_point");


    private String tableName;

    private String type;


    ProjectTypeTableEnum(String type, String tableName) {
        this.tableName = tableName;
        this.type = type;
    }
}
