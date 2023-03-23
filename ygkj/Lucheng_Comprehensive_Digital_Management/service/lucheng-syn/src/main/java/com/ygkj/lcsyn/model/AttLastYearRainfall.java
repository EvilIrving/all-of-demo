package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 常年月平均雨量值（往年的数据月份统计的平均雨量）
 * @date 2022-08-26
 */
@Data
public class AttLastYearRainfall implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雨量站编码
     */
    private String stcd;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 常年测站月平均雨量
     */
    private Double drp;
}
