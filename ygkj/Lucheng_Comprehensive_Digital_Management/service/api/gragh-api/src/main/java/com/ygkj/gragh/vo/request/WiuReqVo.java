package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WiuReqVo {

    @ApiModelProperty("取水户编码")
    private String wiuCode;

    @ApiModelProperty("开始时间")
    private String startYear;

    @ApiModelProperty("结束时间")
    private String endYear;
}
