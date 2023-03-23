package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水资源公报-折合水量
 * @date 2021-07-29
 */
@Data
public class WrbReducedWater implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 折合水量
     */
    private BigDecimal reducedWater;

    public WrbReducedWater() {
    }

}

