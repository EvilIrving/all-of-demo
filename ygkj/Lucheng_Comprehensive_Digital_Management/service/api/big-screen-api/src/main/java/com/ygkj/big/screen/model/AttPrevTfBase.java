package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 山洪防治对象表
 * @date 2021-06-18
 */
@Data
@ApiModel("山洪实体类入参")
public class AttPrevTfBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 地级市
     */
    @ApiModelProperty("地级市")
    private String city;

    /**
     * 区县
     */
    @ApiModelProperty("区县")
    private String county;

    /**
     * 街道
     */
    @ApiModelProperty("街道")
    private String town;

    /**
     * 行政村
     */
    @ApiModelProperty("行政村")
    private String avi;

    /**
     * 自然村
     */
    @ApiModelProperty("自然村")
    private String nvi;

    /**
     * 区域，平方公里
     */
    @ApiModelProperty("区域，平方公里")
    private BigDecimal area;

    /**
     * 户数
     */
    @ApiModelProperty("户数")
    private Integer tho;

    /**
     * 人数
     */
    @ApiModelProperty("人数")
    private Integer tpo;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal lon;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal lat;

    /**
     * 一般村落、重要村落、防治区域
     */
    @ApiModelProperty("一般村落、重要村落、防治区域")
    private String recType;

    /**
     * risk_type
     */
    @ApiModelProperty("risk_type")
    private String riskType;

    /**
     * 防汛责任人
     */
    @ApiModelProperty("防汛责任人")
    private String chargerName;

    /**
     * 防汛电话
     */
    @ApiModelProperty("防汛电话")
    private String chargerPhone;

    /**
     * lvl
     */
    @ApiModelProperty("lvl")
    private String lvl;

    /**
     * 防治区域经纬度坐标点组成的json数组
     */
    @ApiModelProperty("防治区域经纬度坐标点组成的json数组")
    private String areaPoints;

    /**
     * 关联雨量站名称
     */
    @ApiModelProperty("关联雨量站名称")
    private String stationName;

    /**
     * 关联测站代码
     */
    @ApiModelProperty("关联测站代码")
    private String stationCode;

    /**
     * 超过阈值数据
     */
    private double thresholdData;

    /**
     * 超过阈值数据时间
     */
    private String thresholdTime;

    private String warnGradeID;//5 立即转移 4 准备转移

    public AttPrevTfBase() {
    }
}