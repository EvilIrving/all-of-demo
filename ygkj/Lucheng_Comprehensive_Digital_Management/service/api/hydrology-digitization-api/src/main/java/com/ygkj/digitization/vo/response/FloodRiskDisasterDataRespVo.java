package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 洪水风险图灾情数据表
 * @date 2021-07-21
 */
@Data
@ApiModel("洪水风险图灾情数据表")
public class FloodRiskDisasterDataRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 设计洪水标准
     */
    @ApiModelProperty("设计洪水标准")
    private String designStandard;

    /**
     * 淹没面积（k㎡）
     */
    @ApiModelProperty("淹没面积（k㎡）")
    private BigDecimal floodedArea;

    @ApiModelProperty("淹没面积（k㎡）总")
    private BigDecimal floodedAreaTotal;

    /**
     * 淹没人口 （万人）
     */
    @ApiModelProperty("淹没人口 （万人）")
    private BigDecimal submergedPopulation;

    @ApiModelProperty("淹没人口 （万人）总")
    private BigDecimal submergedPopulationTotal;

    /**
     * 淹没区洪水损失（万元）
     */
    @ApiModelProperty("淹没区洪水损失（万元）")
    private BigDecimal floodLoss;

    @ApiModelProperty("淹没区洪水损失（万元）总")
    private BigDecimal floodLossTotal;


    public FloodRiskDisasterDataRespVo() {
    }

}