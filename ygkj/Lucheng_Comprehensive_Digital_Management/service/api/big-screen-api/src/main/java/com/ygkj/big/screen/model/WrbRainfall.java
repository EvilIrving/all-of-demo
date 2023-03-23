package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水资源公报-各区县年度降水量
 * @date 2021-07-29
 */
@Data
public class WrbRainfall implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 行政区划名称
     */
    private String adnm;

    /**
     * 降雨量
     */
    private BigDecimal rainfall;

    public WrbRainfall() {
    }

}
