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
 * @description 近7日出厂水水质实时监测
 * @date 2021-08-23
 */
@Data
@ApiModel("近7日出厂水水质实时监测")
public class CwsWaterQuality implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水厂编码
     */
    @ApiModelProperty("水厂编码")
    private String waterPlantCode;

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

    /**
     * 余氯是否正常
     */
    @ApiModelProperty("余氯是否正常")
    private Integer clNormal;

    /**
     * 浑浊度是否正常
     */
    @ApiModelProperty("浑浊度是否正常")
    private Integer ntuNormal;

    /**
     * ph是否正常
     */
    @ApiModelProperty("ph是否正常")
    private Integer phNormal;

    /**
     * ph实时监测值
     */
    @ApiModelProperty("ph实时监测值")
    private BigDecimal phValue;

    /**
     * 余氯实时监测值
     */
    @ApiModelProperty("余氯实时监测值")
    private BigDecimal clValue;

    /**
     * 浑浊度检测值
     */
    @ApiModelProperty("浑浊度检测值")
    private BigDecimal ntuValue;

    /**
     * 监测时间
     */
    @ApiModelProperty("监测时间")
    private Date timeRecord;

    /**
     * 时间戳
     */
    @ApiModelProperty("时间戳")
    private Date timeStamps;

    /**
     * is_normal
     */
    @ApiModelProperty("is_normal")
    private Integer isNormal;

    public CwsWaterQuality() {
    }

}
