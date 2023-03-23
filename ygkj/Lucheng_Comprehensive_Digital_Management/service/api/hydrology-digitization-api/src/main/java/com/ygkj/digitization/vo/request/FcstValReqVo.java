package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FcstValReqVo {

    @ApiModelProperty("预报id")
    private String fcstId;

    @ApiModelProperty("测站编码")
    private String stCode;
}
