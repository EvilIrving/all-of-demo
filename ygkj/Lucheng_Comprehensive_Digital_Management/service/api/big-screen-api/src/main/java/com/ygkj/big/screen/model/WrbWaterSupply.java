package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水资源公报-各年供水量
 * @date 2021-07-29
 */
@Data
public class WrbWaterSupply implements Serializable {

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
     * 总供水量
     */
    private BigDecimal totalSupply;

    /**
     * 蓄水工程供水量
     */
    private BigDecimal supStoreProj;

    /**
     * 引水工程供水量
     */
    private BigDecimal supDiverProj;

    /**
     * 提水工程供水量
     */
    private BigDecimal supLiftProj;

    public WrbWaterSupply() {
    }

}
