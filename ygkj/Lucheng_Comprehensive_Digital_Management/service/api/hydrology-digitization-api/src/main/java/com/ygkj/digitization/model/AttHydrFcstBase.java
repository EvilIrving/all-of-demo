package com.ygkj.digitization.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水文预报表
 * @date 2021-09-22
 */
@Data
@ApiModel("水文预报表")
public class AttHydrFcstBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 测站编码
     */
    @ApiModelProperty("测站编码")
    private String stCode;

    /**
     * 测站名称
     */
    @ApiModelProperty("测站名称")
    private String stName;

    /**
     * 测站类型
     */
    @ApiModelProperty("测站类型")
    private String stType;

    /**
     * 预报发布时刻
     */
    @ApiModelProperty("预报发布时刻")
    private Date releaseTime;

    /**
     * 洪峰水位m
     */
    @ApiModelProperty("洪峰水位m")
    private BigDecimal peakLevel;

    /**
     * 洪峰水位出现时间
     */
    @ApiModelProperty("洪峰水位出现时间")
    private Date peakLevelTime;

    /**
     * 洪峰流量m³/s
     */
    @ApiModelProperty("洪峰流量m³/s")
    private BigDecimal peakFlow;

    /**
     * 洪峰流量出现时间
     */
    @ApiModelProperty("洪峰流量出现时间")
    private Date peakFlowTime;

    public AttHydrFcstBase() {
    }

}
