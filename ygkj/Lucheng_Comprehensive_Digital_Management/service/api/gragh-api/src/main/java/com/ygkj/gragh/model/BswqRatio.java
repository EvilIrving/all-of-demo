package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description 国考/省控断面水质监测比例
 * @Date 2021/5/7
 */
@Data
public class BswqRatio implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型，1为省控，2为国控
     */
    private int type;

    /**
     * 数据日期
     */
    private Date dataTime;

    /**
     * 当前比例
     */
    private BigDecimal currentNum;

    /**
     * 同比
     */
    private BigDecimal currentNumTb;

    /**
     * 环比
     */
    private BigDecimal currentNumHb;

    public BswqRatio() {
    }

}
