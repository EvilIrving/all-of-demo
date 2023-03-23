package com.ygkj.lcsyn.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StZvarlB {

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 施测时间
     */
    private Date mstm;

    /**
     * 点序号
     */
    private Integer ptno;

    /**
     * 库水位
     */
    private BigDecimal rz;

    /**
     * 蓄水量
     */
    private BigDecimal w;

    /**
     * 水面面积
     */
    private Integer wsfa;

    /**
     * 时间戳
     */
    private Date moditime;
}
