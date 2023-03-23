package com.ygkj.project.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 近7日出厂水水量实时监测
 * @date 2021-08-23
 */
@Data
@ApiModel("近7日出厂水水量实时监测")
public class CwsWaterVolume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水厂编码
     */
    @ApiModelProperty("水厂编码")
    private String waterPlantCode;

    /**
     * 水厂名称
     */
    @ApiModelProperty("水厂名称")
    private String waterPlantName;

    /**
     * 实时流量(m³/h)
     */
    @ApiModelProperty("实时流量(m³/h)")
    private BigDecimal supplyFlow;

    /**
     * 供水状态：0 异常；1 正常
     */
    @ApiModelProperty("供水状态：0 异常；1 正常")
    private Integer supplyStatus;

    /**
     * 区划编码
     */
    @ApiModelProperty("区划编码")
    private String adcd;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 供水开始时间
     */
    @ApiModelProperty("供水开始时间")
    private Date supplyStartTime;

    /**
     * 结束供水时间
     */
    @ApiModelProperty("结束供水时间")
    private Date tongEndTime;

    /**
     * 日累计供水量(m³)
     */
    @ApiModelProperty("日累计供水量(m³)")
    private String totalSupplyQuantity;

    /**
     * 水厂位置（村）
     */
    @ApiModelProperty("水厂位置（村）")
    private String location;

    /**
     * 水厂经度
     */
    @ApiModelProperty("水厂经度")
    private BigDecimal longitude;

    /**
     * 水厂纬度
     */
    @ApiModelProperty("水厂纬度")
    private BigDecimal latitude;

    public CwsWaterVolume() {
    }

}
