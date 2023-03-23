package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WarningRecordNew implements Serializable {
    /**
     * id，统一采用雪花算法策略
     */
    @ApiModelProperty(value = "id，统一采用雪花算法策略")
    private String id;

    /**
     * 报警时间
     */
    @ApiModelProperty(value = "报警时间")
    private Date warningTime;

    /**
     * 报警内容
     */
    @ApiModelProperty(value = "报警内容")
    private String warningContent;

    /**
     * 配置id
     */
    @ApiModelProperty(value = "配置id")
    private String configId;

    /**
     * 测站编码
     */
    @ApiModelProperty(value = "测站编码")
    private String stCode;

    /**
     * 降雨历时
     */
    @ApiModelProperty(value = "降雨历时")
    private String rainfallPeriod;

    /**
     * 降雨量mm
     */
    @ApiModelProperty(value = "降雨量mm")
    private Double rainfall;

    /**
     * 阈值类型
     */
    @ApiModelProperty(value = "阈值类型")
    private String thresholdType;

    /**
     * 水位m
     */
    @ApiModelProperty(value = "水位m")
    private Double waterLevel;

    /**
     * 报警条件
     */
    @ApiModelProperty(value = "报警条件")
    private String warnCondition;

    /**
     * 流量m³/s
     */
    @ApiModelProperty(value = "流量m³/s")
    private Double flow;

    /**
     * 蓄水量
     */
    @ApiModelProperty(value = "蓄水量")
    private Double capacity;

    @ApiModelProperty(value = "转发状态")
    private String forwordStatus;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createdBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updatedBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记")
    private Boolean delFlag;
}
