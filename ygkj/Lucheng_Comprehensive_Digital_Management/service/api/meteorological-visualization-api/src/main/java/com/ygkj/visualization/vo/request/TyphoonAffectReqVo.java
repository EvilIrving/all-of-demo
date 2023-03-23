package com.ygkj.visualization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TyphoonAffectReqVo {

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("行政区划名")
    private String adnm;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("台风名称")
    private String name;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("是否查询全部")
    private boolean fetchAll;

    @ApiModelProperty("当前页数")
    private Integer pageNum = 1;

    @ApiModelProperty("页面显示数")
    private Integer pageSize = 10;
}
