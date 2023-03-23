package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huangzh
 * @description 山洪监测报警值
 * @date 2021-10-11
 */
@Data
@ApiModel("山洪监测报警值")
public class MountainTorrentMonitorWarnValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * nvi，avi，lon，lat 四个属性的哈希值
     */
    @ApiModelProperty("nvi，avi，lon，lat 四个属性的哈希值")
    private Integer hashKey;

    /**
     * 报警监测时刻
     */
    @ApiModelProperty("报警监测时刻")
    private Date warnTm;

    /**
     * 监测指标id
     */
    @ApiModelProperty("监测指标id")
    private String indicatorId;

    /**
     * 实时降雨
     */
    @ApiModelProperty("实时降雨")
    private String realTimeRainfall;

    /**
     * 4，为准备转移，5为立即转移
     */
    @ApiModelProperty("4，为准备转移，5为立即转移")
    private String warnGradeId;

    private Integer warnGradeIdAsInt;

    /**
     * 超过临界值大小
     */
    @ApiModelProperty("超过临界值大小")
    private Double overThresholdVal;

    /**
     * 降雨计算时刻
     */
    @ApiModelProperty("降雨计算时刻")
    private Date tm;

    /**
     * 时段(h)
     */
    @ApiModelProperty("时段(h)")
    private BigDecimal period;


    // 描述用于 兼容预报值
    @ApiModelProperty("")
    private String desc;

    public MountainTorrentMonitorWarnValue() {
    }

    public void buildWarnGradeIdAsInt() {
        try {
            this.warnGradeIdAsInt = Integer.valueOf(warnGradeId);
        } catch (Exception ignore) {

        }
    }
}
