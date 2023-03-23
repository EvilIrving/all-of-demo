package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实时雨量
 */
@Data
public class StPptnR implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stcd;

    @ApiModelProperty("时间")
    private Date tm;

    @ApiModelProperty("雨量")
    private Double drp;

    /**
     * 合并测站编码
     */
    private String mgstcd;

    /**
     * 时段长
     */
    private Double intv;

    /**
     * 上报时间
     */
    private Date uploadTime;

    @ApiModelProperty("测站名称")
    private String stationName;

    private boolean warning;

    private double latitude;

    private double longitude;

    private String stType;

    /**
     * 1小时累计雨量
     */
    private Double rainfallOneData;

    /**
     * 3小时累计雨量
     */
    private Double rainfallThreeData;


    private String bas;

    private String areaName;


    public StPptnR() {
    }
}
