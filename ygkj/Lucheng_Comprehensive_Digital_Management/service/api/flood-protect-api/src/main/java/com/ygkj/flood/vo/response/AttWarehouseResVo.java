package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AttWarehouseResVo {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 仓库名称
     */
    @ApiModelProperty("仓库名称")
    private String name;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

    /**
     * 行政区划名称（冗余字段，避免无谓的联表）
     */
    @ApiModelProperty("行政区划名称")
    private String areaName;

    @ApiModelProperty("地理位置")
    private String geographicalPosition;


    @ApiModelProperty("详细地址")
    private String address;


    @ApiModelProperty("经度")
    private String longitude;


    @ApiModelProperty("纬度")
    private String latitude;


    @ApiModelProperty("仓库类型名称")
    private String type;


    @ApiModelProperty("物资价值")
    private String materialPrice;


    @ApiModelProperty("仓库面积(㎡）")
    private BigDecimal acreage;


    @ApiModelProperty("仓储条件")
    private String storageConditions;


    @ApiModelProperty("仓库结构")
    private String warehouseStructure;


    @ApiModelProperty("交通条件")
    private String trafficConditions;


    @ApiModelProperty("仓库物资")
    private String warehouseMaterials;


    @ApiModelProperty("建造时间")
    private Date bulidTime;


    @ApiModelProperty("备注信息")
    private String remarkInformation;


    @ApiModelProperty("负责人名称")
    private String chargePerson;


    @ApiModelProperty("负责人联系方式")
    private String chargePhone;
}
