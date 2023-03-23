package com.ygkj.project.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 海塘实时监测报警数据表
 * @date 2021-10-10
 */
@Data
public class SeawallWarnRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 海塘编码
     */
    private String seawallCode;

    /**
     * 报警时刻
     */
    private Date warnTm;

    /**
     * 实时潮位
     */
    private Double tdz;

    /**
     * 报警级别：1：蓝色；2：黄色；3：橙色；4：红色
     */
    private int warnState;

    /**
     * 潮位数据时刻
     */
    private Date tdzTm;

    public SeawallWarnRecord() {
    }

}
