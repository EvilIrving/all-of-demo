package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("泵站列表出参")
public class PustListResVo {

    @ApiModelProperty("泵站代码")
    private String pustCode;

    @ApiModelProperty("泵站名称")
    private String pustName;

    @ApiModelProperty("经度")
    private BigDecimal pustLong;

    @ApiModelProperty("纬度")
    private BigDecimal pustLat;

    @ApiModelProperty("上报时间")
    private String tm;
}
