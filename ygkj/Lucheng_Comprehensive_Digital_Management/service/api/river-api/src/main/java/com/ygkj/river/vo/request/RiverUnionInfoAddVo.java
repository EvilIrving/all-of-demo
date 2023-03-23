package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RiverUnionInfoAddVo {

    /**
     * id
     */
    @ApiModelProperty("id，修改用")
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 类型：河道河景/雨水口/入河排污口...
     */
    @ApiModelProperty("类型：河道河景/雨水口/入河排污口...")
    private String type;

    /**
     * 行政区划名称
     */
    @ApiModelProperty("行政区划名称")
    private String areaName;

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String basin;

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
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 附件id
     */
    @ApiModelProperty("附件id")
    private String fileIds;

    @ApiModelProperty("所属河道id")
    private String riverId;
    @ApiModelProperty("所属河道名称")
    private String riverName;
}
