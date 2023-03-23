package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 水资源公报-各年大中型水库蓄水量
 * @date 2021-07-29
 */
@Data
public class WrbStoreRsvr implements Serializable {

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
     * 年末蓄水量
     */
    private BigDecimal endYearStore;

    /**
     * 水库名称
     */
    private String resName;

    /**
     * 水库编码
     */
    private String resCode;

    public WrbStoreRsvr() {
    }

}
