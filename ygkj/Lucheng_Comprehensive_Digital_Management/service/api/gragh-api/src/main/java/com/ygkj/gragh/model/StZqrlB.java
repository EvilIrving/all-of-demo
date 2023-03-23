package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 水位流量关系曲线表, 用于存储测站测验断面水位和流量的相关关系
 * @date 2021-06-21
 */
@Data
public class StZqrlB implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合并测站编码
     */
    private String mgstcd;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 曲线名称
     */
    private String lnnm;

    /**
     * 启用时间
     */
    private Date bgtm;

    /**
     * 点序号
     */
    private Integer ptno;

    /**
     * 水位，单位m
     */
    private BigDecimal z;

    /**
     * 流量，单位m³/s
     */
    private BigDecimal q;

    /**
     * 备注
     */
    private String comments;

    /**
     * 时间戳
     */
    private Date moditime;

    public StZqrlB() {
    }

}
