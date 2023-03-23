package com.ygkj.big.screen.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 山洪监测报警记录
 * @date 2021-10-11
 */
@Data
@ApiModel("山洪监测报警记录")
public class MountainTorrentMonitorWarnRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * nvi，avi，lon，lat 四个属性的哈希值
     */
    @ApiModelProperty("nvi，avi，lon，lat 四个属性的哈希值")
    private Integer hashKey;

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
     * 报警监测时刻
     */
    @ApiModelProperty("报警监测时刻")
    private Date warnTm;

    /**
     * 报警的json
     */
    @ApiModelProperty("报警的json")
    private String warnValue;

    public MountainTorrentMonitorWarnRecord() {
    }

}

