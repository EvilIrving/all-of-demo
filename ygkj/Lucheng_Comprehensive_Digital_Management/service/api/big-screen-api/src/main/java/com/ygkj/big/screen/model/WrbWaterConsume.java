package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水资源公报-各区县各年用水量
 * @date 2021-07-29
 */
@Data
public class WrbWaterConsume implements Serializable {

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
     * 总用水量
     */
    private BigDecimal totalConsume;

    /**
     * 农业灌溉用水量
     */
    private BigDecimal conArgIrr;

    /**
     * 林牧渔畜用水量
     */
    private BigDecimal conFahf;

    /**
     * 工业用水量
     */
    private BigDecimal conIndu;

    /**
     * 城镇公共用水量
     */
    private BigDecimal conCityPub;

    /**
     * 居民生活用水量
     */
    private BigDecimal conResidLife;

    /**
     * 生态与环境用水量
     */
    private BigDecimal conEcoEnv;

    public WrbWaterConsume() {
    }

}

