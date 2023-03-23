package com.ygkj.visualization.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttRainGrid implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 行号
     */
    private Integer rowIndex;

    /**
     * 列号
     */
    private Integer columnIndex;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 行政区划名称
     */
    private String areaName;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 流域
     */
    private String basin;

    private double rainfall;

    private String townName;

    private String townAreaCode;


    public AttRainGrid() {
    }

}
