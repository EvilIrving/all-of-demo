package com.ygkj.lcsyn.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StRsvrfcchB {

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 水库类型
     */
    private String rsvrtp;

    /**
     * 坝顶高程
     */
    private BigDecimal damel;

    /**
     * 校核洪水位
     */
    private BigDecimal ckflz;

    /**
     * 设计洪水位
     */
    private BigDecimal dsflz;

    /**
     * 正常高水位
     */
    private BigDecimal normz;

    /**
     * 死水位
     */
    private BigDecimal ddz;

    /**
     * 兴利水位
     */
    private BigDecimal actz;

    /**
     * 总库容
     */
    private BigDecimal ttcp;

    /**
     * 防洪库容
     */
    private BigDecimal fldcp;

    /**
     * 兴利库容
     */
    private BigDecimal actcp;

    /**
     * 死库容
     */
    private BigDecimal ddcp;

    /**
     * 历史最高库水位
     */
    private BigDecimal hhrz;

    /**
     * 历史最大蓄水量
     */
    private BigDecimal hmxw;

    /**
     * 历史最高库水位（蓄水量）出现时间
     */
    private Date hhrztm;

    /**
     * 历史最大入流
     */
    private BigDecimal hmxinq;

    /**
     * 历史最大入流时段长
     */
    private BigDecimal rstdr;

    /**
     * 历史最大入流出现时间
     */
    private Date hmxinqtm;

    /**
     * 历史最大出流
     */
    private BigDecimal hmxotq;

    /**
     * 历史最大出流出现时间
     */
    private Date hmxotqtm;

    /**
     * 历史最低库水位
     */
    private BigDecimal hlrz;

    /**
     * 历史最低库水位出现时间
     */
    private Date hlrztm;

    /**
     * 历史最小日均入流
     */
    private BigDecimal hmninq;

    /**
     * 历史最小日均入流出现时间
     */
    private Date hmninqtm;

    /**
     * 低水位告警值
     */
    private BigDecimal laz;

    /**
     * 启动预报流量标准
     */
    private BigDecimal sfq;

    /**
     * 时间戳
     */
    private Date moditime;
}
