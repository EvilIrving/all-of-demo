package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 15:30
 * @description 农村供水工程
 */
@Data
@ApiModel("农村供水工程")
public class WaterSupplyProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("工程编码")
    private String projectCode;


    @ApiModelProperty("project_name")
    private String projectName;


    @ApiModelProperty("project_type")
    private String projectType;


    @ApiModelProperty("longitude")
    private String longitude;


    @ApiModelProperty("latitude")
    private String latitude;


    @ApiModelProperty("del_flag")
    private int delFlag;


    @ApiModelProperty("muti")
    private String muti;


    @ApiModelProperty("image_url")
    private String imageUrl;


    @ApiModelProperty("image_show")
    private String imageShow;


    @ApiModelProperty("design_water")
    private String designWater;


    @ApiModelProperty("true_water")
    private String trueWater;


    @ApiModelProperty("water_location")
    private String waterLocation;


    @ApiModelProperty("water_permit")
    private String waterPermit;


    @ApiModelProperty("facility")
    private String facility;


    @ApiModelProperty("fa_normal")
    private String faNormal;


    @ApiModelProperty("equipment")
    private String equipment;


    @ApiModelProperty("eq_normal")
    private String eqNormal;


    @ApiModelProperty("quality_test")
    private String qualityTest;


    @ApiModelProperty("unitpid")
    private String unitpid;


    @ApiModelProperty("person_name")
    private String personName;


    @ApiModelProperty("person_telphone")
    private String personTelphone;


    @ApiModelProperty("person_location")
    private String personLocation;


    @ApiModelProperty("administration")
    private String administration;


    @ApiModelProperty("p_name")
    private String pName;


    @ApiModelProperty("pe_telpho")
    private String peTelpho;


    @ApiModelProperty("p_location")
    private String pLocation;


    @ApiModelProperty("protectionid")
    private String protectionid;


    @ApiModelProperty("allunitid")
    private String allunitid;


    @ApiModelProperty("probaseid")
    private String probaseid;


    @ApiModelProperty("county")
    private String county;


    @ApiModelProperty("town")
    private String town;


    @ApiModelProperty("administrativevillage")
    private String administrativevillage;


    @ApiModelProperty("naturalvillage")
    private String naturalvillage;


    @ApiModelProperty("adcd")
    private String adcd;


    @ApiModelProperty("update_at")
    private String updateAt;
}
