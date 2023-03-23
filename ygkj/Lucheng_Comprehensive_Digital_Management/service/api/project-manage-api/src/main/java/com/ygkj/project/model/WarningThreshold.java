package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WarningThreshold implements Serializable {
    /**
     * id，统一采用雪花算法策略
     */
    @ApiModelProperty(value = "id，统一采用雪花算法策略")
    private String id;

    /**
     * 报警配置id
     */
    @ApiModelProperty(value = "报警配置id")
    private String configId;

    /**
     * 水文监测站代码
     */
    @ApiModelProperty(value = "水文监测站代码")
    private String stCode;

    /**
     * 降雨历时
     */
    @ApiModelProperty(value = "降雨历时")
    private String rainfallPeriod;

    /**
     * 阈值mm
     */
    @ApiModelProperty(value = "阈值mm")
    private Double threshold;

    /**
     * 阈值类型(水库：汛限水位、防洪高水位;闸坝和河道：警戒水位、保证水位;潮位：蓝色警戒水位、黄色警戒水位、橙色警戒水位、红色警戒水位)
     */
    @ApiModelProperty(value = "阈值类型(水库：汛限水位、防洪高水位;闸坝和河道：警戒水位、保证水位;潮位：蓝色警戒水位、黄色警戒水位、橙色警戒水位、红色警戒水位)")
    private String thresholdType;

    /**
     * 站点类型(RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站)
     */
    @ApiModelProperty(value = "站点类型(RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站)")
    private String stType;

    /**
     * 报警条件
     */
    @ApiModelProperty(value = "报警条件")
    private String warnCondition;

    /**
     * 流量阈值m³/s
     */
    @ApiModelProperty(value = "流量阈值m³/s")
    private Double flowThreshold;

    /**
     * 平均蓄水量m³
     */
    @ApiModelProperty(value = "平均蓄水量m³")
    private Double avgCapacity;

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
