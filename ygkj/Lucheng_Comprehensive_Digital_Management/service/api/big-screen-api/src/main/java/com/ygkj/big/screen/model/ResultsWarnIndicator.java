package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 山洪灾害防治区重要村落预警指标成果表
 * @date 2021-06-18
 */
@Data
public class ResultsWarnIndicator implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 街道
     */
    private String town;

    /**
     * 村落
     */
    private String avi;

    /**
     * 自然村
     */
    private String nvi;

    /**
     * 土壤含水量/前期降雨
     */
    private String soilMoisture;

    /**
     * 指标类别
     */
    private String indexCategory;

    /**
     * 时段(h)
     */
    private BigDecimal period;

    /**
     * 准备转移
     */
    private BigDecimal readyMove;

    /**
     * 立即转移
     */
    private BigDecimal immediateTransfer;

    /**
     * 临界雨量/水位
     */
    private BigDecimal criticalLevel;

    /**
     * 方法
     */
    private String method;

    public ResultsWarnIndicator() {
    }
}