package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水库库容曲线
 *
 * @author ljx 2021-01-25
 */
@Data
public class ReservoirCapacityCurve implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * code 主测站编码
     */
    private String code;

    /**
     * water_level 水位
     */
    private BigDecimal waterLevel;

    /**
     * capacity
     */
    private BigDecimal capacity;

    public ReservoirCapacityCurve() {
    }

}
