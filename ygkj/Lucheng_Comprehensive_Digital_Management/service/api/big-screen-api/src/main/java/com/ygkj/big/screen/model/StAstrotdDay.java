package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 天文潮日最高最低潮位
 * @date 2021-06-09
 */
@Data
public class StAstrotdDay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    private Date date;

    /**
     * 上午最高值时刻
     */
    private Date amHighTime;

    /**
     * 上午最高值
     */
    private String amHighValue;

    /**
     * 上午最低值时刻
     */
    private Date amLowTime;

    /**
     * 上午最低值
     */
    private String amLowValue;

    /**
     * 下午最高值时刻
     */
    private Date pmHighTime;

    /**
     * 下午最高值
     */
    private String pmHighValue;

    /**
     * 下午最低值时刻
     */
    private Date pmLowTime;

    /**
     * 下午最低值
     */
    private String pmLowValue;

    /**
     * 测站名称
     */
    private String stnm;

    public StAstrotdDay() {
    }

}