package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RiverUnionInfoPageQueryVo {

    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("每页限定记录数")
    private Integer pageSize;
    @ApiModelProperty("id,查询单条用")
    private String id;
    @ApiModelProperty("行政区划名称")
    private String areaName;
    @ApiModelProperty("行政区划编码")
    private String areaCode;
    @ApiModelProperty("所属流域")
    private String basin;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("类型：河道河景/雨水口/入河排污口")
    private String type;
}
