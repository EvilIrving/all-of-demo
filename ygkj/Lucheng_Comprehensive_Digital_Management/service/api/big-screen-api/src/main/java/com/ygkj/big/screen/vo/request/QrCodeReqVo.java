package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCodeReqVo {

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("风暴潮id")
    private String fcstId;
}
