package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水资源公报-各区县各年水资源量
 * @date 2021-07-29
 */
@Data
public class WrbWaterResource implements Serializable {

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
     * 水资源总量
     */
    private BigDecimal totalWaterResource;

    /**
     * 地表水量
     */
    private BigDecimal surfaceWater;

    /**
     * 地下水量
     */
    private BigDecimal groundWater;

    /**
     * 全市平均水资源利用率
     */
    private BigDecimal avgWrUseRate;

    /**
     * 人均用水水资源量
     */
    private BigDecimal avgWrUse;

    /**
     * 产水系数
     */
    private BigDecimal waterProduceFactor;

    /**
     * 产水模数
     */
    private BigDecimal waterProduceMod;

    public WrbWaterResource() {
    }

}
