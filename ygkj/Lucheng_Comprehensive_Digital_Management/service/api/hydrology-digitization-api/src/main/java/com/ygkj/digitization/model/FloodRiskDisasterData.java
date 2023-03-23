package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 洪水风险图灾情数据表
 * @date 2021-07-21
 */
@Data
public class FloodRiskDisasterData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 设计洪水标准
     */
    private String designStandard;

    /**
     * 淹没面积（k㎡）
     */
    private BigDecimal floodedArea;

    /**
     * 淹没人口 （万人）
     */
    private BigDecimal submergedPopulation;

    /**
     * 淹没区洪水损失（万元）
     */
    private BigDecimal floodLoss;

    /**
     * 删除标识
     */
    private String delFlag;

    public FloodRiskDisasterData() {
    }

}
