package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 15:38
 * @description 防汛仓库信息
 */
@Data
@ApiModel("防汛仓库信息")
public class AttWarehouseBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("仓库名称")
    private String name;


    @ApiModelProperty("行政区划编码")
    private String areaCode;


    @ApiModelProperty("行政区划名称（冗余字段，避免无谓的联表）")
    private String areaName;


    @ApiModelProperty("详细地址")
    private String address;


    @ApiModelProperty("经度")
    private String longitude;


    @ApiModelProperty("纬度")
    private String latitude;


    @ApiModelProperty("仓库类型id，关联字典表")
    private String typeId;


    @ApiModelProperty("仓库类型名称（冗余字段，避免无谓的联表）")
    private String type;


    @ApiModelProperty("物资价值")
    private String materialPrice;


    @ApiModelProperty("负责人名称")
    private String chargePerson;


    @ApiModelProperty("负责人联系方式")
    private String chargePhone;


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


    @ApiModelProperty("仓库面积(㎡）")
    private Double acreage;
}
