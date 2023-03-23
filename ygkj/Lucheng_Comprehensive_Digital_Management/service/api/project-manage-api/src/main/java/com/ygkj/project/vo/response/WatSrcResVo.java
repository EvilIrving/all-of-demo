package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xq
 * @Description
 * @Date 2021/8/11
 */
@Data
@ApiModel("水源保障回参")
public class WatSrcResVo {

    transient final static BigDecimal hundred = BigDecimal.valueOf(100);

    @ApiModelProperty("水库编码")
    String code;

    @ApiModelProperty("水源地名称，即水库名称")
    String name;

    @ApiModelProperty("纬度")
    String latitude;

    @ApiModelProperty("经度")
    String longitude;

    @ApiModelProperty("实时水位")
    Double waterLevel;

    @ApiModelProperty("主测站编码")
    String mainStcd;

    @ApiModelProperty("实时水位对应的库容(万m³)")
    BigDecimal currentCapacity;

    @ApiModelProperty("日供水量(万m³)")
    BigDecimal dayWatSupply;

    @ApiModelProperty("旱限水位(米)")
    BigDecimal dryLimitLevel;

    @ApiModelProperty("死水位(米)")
    BigDecimal deadLev;

    @ApiModelProperty("旱限水位对应库容(万m³)")
    BigDecimal dryLimitCapacity;

    @ApiModelProperty("死水位对应库容(万m³)")
    BigDecimal deadLevCapacity;

    @ApiModelProperty("兴利下限水位(米)")
    BigDecimal benLowerLevel;

    /**
     * 水源可供水量（水库）=实时水位对应的库容-死水位对应库容
     * supportWater=currentCapacity-dryLimitCapacity
     */
    @ApiModelProperty("水源可用水量（万m³）")
    BigDecimal supportWater;

    /**
     * 供水能力=水源可供水量/日供水量
     * supplyAbility=supportWater/dayWatSupply
     */
    @ApiModelProperty("供水能力（天）")
    Integer supplyAbility;

    @ApiModelProperty("供水规模")
    String engType;

    @ApiModelProperty("行政区划")
    String areaName;

    @ApiModelProperty("是否划定保护区")
    String protectArea;

    @ApiModelProperty("目标水质")
    String targetQuality;

    @ApiModelProperty("水量预警")
    String volumeWarn;


    /**
     * 积水可供水量
     * 水源可供水量（水库）=实时水位对应的库容-死水位对应库容
     * supportWater=currentCapacity-dryLimitCapacity
     */
    public void computeSupportWater() {
        // 库容是百万立方米换算成万立方米需要乘以一百
        if (deadLevCapacity != null && currentCapacity != null) {
            supportWater = currentCapacity.subtract(deadLevCapacity);
        }
    }

    /**
     * 计算供水能力
     * 供水能力=水源可供水量/日供水量
     * supplyAbility=supportWater/dayWatSupply
     */
    public void computeSupplyAbility() {
        if (supportWater != null && dayWatSupply != null) {
            supplyAbility = supportWater.divide(dayWatSupply, 0, BigDecimal.ROUND_HALF_UP).intValue();
        }
    }


}
