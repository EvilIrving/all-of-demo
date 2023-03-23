package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CPU信息
 */
@Data
public class Cpu implements Serializable {

    /**
     * 主键id
     */
    private String id;
    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总
     */
    private BigDecimal total;

    /**
     * CPU系统使用率
     */
    private BigDecimal sys;

    /**
     * CPU用户使用率
     */
    private BigDecimal used;

    /**
     * CPU当前等待率
     */
    private BigDecimal wait;

    /**
     * CPU当前空闲率
     */
    private BigDecimal free;

    private String delFlag;

    private Date createTime;

    public Cpu() {
    }
}