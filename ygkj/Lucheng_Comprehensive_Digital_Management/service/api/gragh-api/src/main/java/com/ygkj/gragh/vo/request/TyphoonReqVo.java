package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TyphoonReqVo {

    @ApiModelProperty("每页展示条数")
    private int pageSize;

    @ApiModelProperty("页码")
    private int pageNum;

    @ApiModelProperty("台风开始时间的起始时间")
    private String startTime;

    @ApiModelProperty("台风开始时间的结束时间")
    private String endTime;

    @ApiModelProperty("台风名称")
    private String name;
}
