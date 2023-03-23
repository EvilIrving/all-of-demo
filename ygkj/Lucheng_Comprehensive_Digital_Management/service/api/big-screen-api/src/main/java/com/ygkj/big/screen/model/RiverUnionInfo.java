package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 河道关联项目表
 */
@Data
public class RiverUnionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("名称")
    private String name;


    @ApiModelProperty("类型：河道河景/雨水口/入河排污口...")
    private String type;


    @ApiModelProperty("行政区划名称")
    private String areaName;


    @ApiModelProperty("所属流域")
    private String basin;


    @ApiModelProperty("经度")
    private String longitude;


    @ApiModelProperty("纬度")
    private String latitude;


    @ApiModelProperty("描述")
    private String description;


    @ApiModelProperty("附件id")
    private String fileIds;


    @ApiModelProperty("所属河道id")
    private String riverId;


    @ApiModelProperty("所属河道名称")
    private String riverName;
}
