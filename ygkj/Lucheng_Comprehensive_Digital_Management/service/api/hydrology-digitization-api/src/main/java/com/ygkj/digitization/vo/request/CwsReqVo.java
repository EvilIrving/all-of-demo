package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CwsReqVo {

    @ApiModelProperty("测站编码")
    private String stationCode;

    @ApiModelProperty("天数")
    private Integer day;
}
