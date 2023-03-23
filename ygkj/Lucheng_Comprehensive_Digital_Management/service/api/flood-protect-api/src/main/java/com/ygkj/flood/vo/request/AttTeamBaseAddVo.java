package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttTeamBaseAddVo {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("队伍名称")
    private String name;

    /**
     * 队伍类型id，关联字典表，编码为teamtype
     */
    @ApiModelProperty("队伍类型id")
    private String typeId;

    /**
     * 队伍类型（冗余字段，避免无谓的表级联）
     */
    @ApiModelProperty("队伍类型")
    private String typeName;

    /**
     * 队伍详细地址
     */
    @ApiModelProperty("队伍详细地址")
    private String address;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String latitude;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

    /**
     * 行政区划名称（冗余字段，避免无谓的表级联）
     */
    @ApiModelProperty("行政区划名称")
    private String areaName;

    /**
     * 负责人姓名
     */
    @ApiModelProperty("负责人姓名")
    private String chargePerson;

    /**
     * 负责人联系电话
     */
    @ApiModelProperty("负责人联系电话")
    private String chargePhone;

    /**
     * 编制人数
     */
    @ApiModelProperty("编制人数")
    private Integer organizationNumber;

    /**
     * 现有人数
     */
    @ApiModelProperty("现有人数")
    private Integer existingNumber;

    @ApiModelProperty("专业性质")
    private String technicality;


    @ApiModelProperty("现有资产（万元）")
    private String existingAssets;

    @ApiModelProperty("单位性质")
    private String unitNature;


    @ApiModelProperty("抢险专业")
    private String rescueSpecialty;


    @ApiModelProperty("组织形式")
    private String organizationalForm;


    @ApiModelProperty("救援领域")
    private String rescueField;


    @ApiModelProperty("救援规模能力")
    private String rescueCapability;


    @ApiModelProperty("传真")
    private String fax;

    @ApiModelProperty("备注")
    private String remarks;
}
