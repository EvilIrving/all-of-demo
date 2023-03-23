package com.ygkj.project.vo.response;

import com.ygkj.project.model.AttCwsBaseWcp;
import com.ygkj.project.model.CwsWaterQuality;
import com.ygkj.project.model.CwsWaterVolume;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xq
 * @Description
 * @Date 2021/8/23
 */
@Data
@ApiModel("水厂回参")
public class CwsWcpResVo extends AttCwsBaseWcp {
    private static final long serialVersionUID = -3337901392333312269L;

    @ApiModelProperty("运行状态，0，正常，1供水预警，2水质预警，3水质供水预警")
    int runtimeState = 0;
    @ApiModelProperty("水量状态：0，异常，1，正常")
    int volumeNormal = 1;
    @ApiModelProperty("水质状态：0，异常，1，正常")
    int qualityNormal = 1;

    @ApiModelProperty("水质")
    CwsWaterQuality quality;

    @ApiModelProperty("水量")
    CwsWaterVolume volume;

    @ApiModelProperty("行政区划")
    String adnm;


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
     * 实时流量(m³/h)
     */
    @ApiModelProperty("实时流量(m³/h)")
    private BigDecimal supplyFlow;

    /**
     * 供水状态：0 异常；1 正常
     */
    @ApiModelProperty("供水状态：0 异常；1 正常")
    private Integer volumeSupplyStatus;

    /**
     * 日累计供水量(m³)
     */
    @ApiModelProperty("日累计供水量(m³)")
    private String totalSupplyQuantity;


    public void setQuality(CwsWaterQuality quality) {
        this.quality = quality;
        if (quality != null) {
            this.clNormal = quality.getClNormal();
            this.ntuNormal = quality.getNtuNormal();
            this.phNormal = quality.getPhNormal();
            this.phValue = quality.getPhValue();
            this.clValue = quality.getClValue();
            this.ntuValue = quality.getNtuValue();
        }
    }

    public void setVolume(CwsWaterVolume volume) {
        this.volume = volume;
        if (volume != null) {
            this.supplyFlow = volume.getSupplyFlow();
            this.volumeSupplyStatus = volume.getSupplyStatus();
            this.totalSupplyQuantity = volume.getTotalSupplyQuantity();
        }
    }

    public void computeRuntimeState() {
        // 1 表示正常 0表示异常
        if (volume != null && volume.getSupplyStatus() != 1) {
            volumeNormal = 0;
        }
        if (quality != null) {
            if (quality.getClNormal() != null && quality.getClNormal() != 1) {
                qualityNormal = 0;
            }
            if (quality.getNtuNormal() != null && quality.getNtuNormal() != 1) {
                qualityNormal = 0;
            }
            if (quality.getPhNormal() != null && quality.getPhNormal() != 1) {
                qualityNormal = 0;
            }
        }
        if (volumeNormal == 1 && qualityNormal == 1) {
            runtimeState = 0;
        } else if (volumeNormal == 1 && qualityNormal == 0) {
            runtimeState = 1;
        } else if (volumeNormal == 0 && qualityNormal == 1) {
            runtimeState = 2;
        } else if (volumeNormal == 0 && qualityNormal == 0) {
            runtimeState = 3;
        }
    }

}
