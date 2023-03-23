package com.ygkj.gragh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/9/1
 */
@Data
public class PrecedeAffectRainfall {
    /**
     * 行政区划编码
     */
    String areaCode;

    /**
     * 行政区划名称
     */
    String areaName;

    /**
     * 日期
     */
    Date date;

    /**
     * 前期影响雨量
     */
    BigDecimal drp;

    @ApiModelProperty("3 最大值，2 正常值，1最小值")
    Integer valueType = 2;
}
