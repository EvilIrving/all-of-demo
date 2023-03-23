package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WiuResVo {

    @ApiModelProperty("取水户编码")
    private String wiuCode;

    @ApiModelProperty("取水户名称")
    private String wiuName;

    @ApiModelProperty("取水户经度")
    private BigDecimal wiuLong;

    @ApiModelProperty("取水户纬度")
    private BigDecimal wiuLat;

    @ApiModelProperty("计划取水量")
    private BigDecimal watPlan;

    @ApiModelProperty("累计取水量")
    private BigDecimal watUse;

    @ApiModelProperty("取水量状态 1正常 2接近取水计划 3超过取水计划")
    private Integer type;

    @ApiModelProperty("城市名称")
    private String cityAdnm;

    @ApiModelProperty("县（区）名称")
    private String countryAdnm;
}
