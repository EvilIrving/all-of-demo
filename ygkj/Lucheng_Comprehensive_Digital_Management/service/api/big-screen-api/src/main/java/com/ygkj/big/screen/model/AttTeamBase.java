package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 15:40
 * @description 抢险队伍信息
 */
@Data
@ApiModel("抢险队伍信息")
public class AttTeamBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("队伍名称")
    private String name;


    @ApiModelProperty("队伍类型id，关联字典表，编码为teamtype")
    private String typeId;


    @ApiModelProperty("队伍类型（冗余字段，避免无谓的表级联）")
    private String typeName;


    @ApiModelProperty("队伍详细地址")
    private String address;


    @ApiModelProperty("经度")
    private String longitude;


    @ApiModelProperty("纬度")
    private String latitude;


    @ApiModelProperty("行政区划编码")
    private String areaCode;


    @ApiModelProperty("行政区划名称（冗余字段，避免无谓的表级联）")
    private String areaName;


    @ApiModelProperty("负责人姓名")
    private String chargePerson;


    @ApiModelProperty("负责人联系电话")
    private String chargePhone;


    @ApiModelProperty("编制人数")
    private Integer organizationNumber;


    @ApiModelProperty("现有人数")
    private Integer existingNumber;


    @ApiModelProperty("创建时间")
    private String createTime;


    @ApiModelProperty("创建人")
    private String createId;


    @ApiModelProperty("修改时间")
    private String updateTime;


    @ApiModelProperty("修改人")
    private String updateId;


    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;


    @ApiModelProperty("专业性质")
    private String technicality;


    @ApiModelProperty("现有资产（万元）")
    private String existingAssets;
}
