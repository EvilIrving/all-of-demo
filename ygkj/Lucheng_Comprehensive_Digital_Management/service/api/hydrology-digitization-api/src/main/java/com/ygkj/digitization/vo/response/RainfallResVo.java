package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiOperation("各测站降雨量出参")
public class RainfallResVo {

    @ApiModelProperty("降雨量")
    private Double drp;

    @ApiModelProperty("经度")
    private BigDecimal stLong;

    @ApiModelProperty("纬度")
    private BigDecimal stLat;

    @ApiModelProperty("测站编码")
    private String stcd;

    @ApiModelProperty("日期")
    private String date;
}
