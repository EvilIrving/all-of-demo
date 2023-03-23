package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 河网库容曲线
 *
 * @author xq 2021-05-27
 */
@Data
public class RivernetCapacityCurve implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * code 水库编码
     */
    private String code;

    /**
     * water_level 水位
     */
    private BigDecimal waterLevel;

    /**
     * capacity 库容
     */
    private BigDecimal capacity;

    public RivernetCapacityCurve() {
    }

}
