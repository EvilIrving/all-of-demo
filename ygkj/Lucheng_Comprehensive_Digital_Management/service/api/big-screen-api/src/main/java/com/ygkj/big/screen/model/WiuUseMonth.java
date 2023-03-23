package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 取水户月已用水量
 */
@Data
public class WiuUseMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("取水户编码")
    private String wiuCode;

    @ApiModelProperty("累计取水年份")
    private String year;

    @ApiModelProperty("目前已用水量(m³)")
    private Double watUse;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("月份")
    private String month;
}
