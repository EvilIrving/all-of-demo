package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: huangzh
 * @Date: 2020/9/8 10:54
 * @Description:
 */
@Data
public class WaterBaseByInfoPageReqVo {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize=10;

    @ApiModelProperty(value = "行政区划")
    private String areaName;

    @ApiModelProperty(value = "水域类型")
    private String waterType;

    @ApiModelProperty(value = "水域面积")
    private Double waterArea;

    @ApiModelProperty(value = "岸线信息")
    private String shorelineInfo;

    @ApiModelProperty(value = "水域名称")
    private String waterName;
}
