package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 泵站工情表
 * @date 2021-09-03
 */
@Data
public class StPustR implements Serializable {

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
     * 测量时间
     */
    private Date tm;

    /**
     * 机组1开关情况
     */
    private String num1;

    /**
     * 机组2开关情况
     */
    private String num2;

    /**
     * 机组3开关情况
     */
    private String num3;

    /**
     * 机组4开关情况
     */
    private String num4;

    /**
     * 机组5开关情况
     */
    private String num5;

    /**
     * 进水口水位
     */
    private Double jskZ;

    /**
     * 进水池水位
     */
    private Double jscZ;

    /**
     * 出水池水位
     */
    private Double cscZ;

    /**
     * 上报时间
     */
    private Date uploadTime;

    /**
     * 流量
     */
    private String sum;

    public StPustR() {
    }

}