package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AverageRainfall implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 行政区划名称
     */
    private String areaName;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 平均降雨量
     */
    private Double rainfall;

    /**
     * 测站编码
     */
    private String mgstcd;

    public AverageRainfall() {
    }

}

