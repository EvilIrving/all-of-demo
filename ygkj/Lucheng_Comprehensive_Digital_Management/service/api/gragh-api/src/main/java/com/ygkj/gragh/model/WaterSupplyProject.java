package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 农村供水工程
 * @date 2020-12-09
 */
@Data
public class WaterSupplyProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * project_name
     */
    private String projectName;

    /**
     * project_type
     */
    private String projectType;

    /**
     * longitude
     */
    private String longitude;

    /**
     * latitude
     */
    private String latitude;

    /**
     * del_flag
     */
    private boolean delFlag;

    /**
     * muti
     */
    private String muti;

    /**
     * image_url
     */
    private String imageUrl;

    /**
     * image_show
     */
    private String imageShow;

    /**
     * design_water
     */
    private String designWater;

    /**
     * true_water
     */
    private String trueWater;

    /**
     * water_location
     */
    private String waterLocation;

    /**
     * water_permit
     */
    private String waterPermit;

    /**
     * facility
     */
    private String facility;

    /**
     * fa_normal
     */
    private String faNormal;

    /**
     * equipment
     */
    private String equipment;

    /**
     * eq_normal
     */
    private String eqNormal;

    /**
     * quality_test
     */
    private String qualityTest;

    /**
     * unitpid
     */
    private String unitpid;

    /**
     * person_name
     */
    private String personName;

    /**
     * person_telphone
     */
    private String personTelphone;

    /**
     * person_location
     */
    private String personLocation;

    /**
     * administration
     */
    private String administration;

    /**
     * p_name
     */
    private String pName;

    /**
     * pe_telpho
     */
    private String peTelpho;

    /**
     * p_location
     */
    private String pLocation;

    /**
     * protectionid
     */
    private String protectionid;

    /**
     * allunitid
     */
    private String allunitid;

    /**
     * probaseid
     */
    private String probaseid;

    /**
     * county
     */
    private String county;

    /**
     * town
     */
    private String town;

    /**
     * administrativevillage
     */
    private String administrativevillage;

    /**
     * naturalvillage
     */
    private String naturalvillage;

    /**
     * adcd
     */
    private String adcd;

    /**
     * update_at
     */
    private Date updateAt;

    public WaterSupplyProject() {
    }

}

