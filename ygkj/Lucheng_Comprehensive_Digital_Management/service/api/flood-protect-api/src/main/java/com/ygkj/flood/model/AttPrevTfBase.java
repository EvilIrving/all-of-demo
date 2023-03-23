package com.ygkj.flood.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Huangzh
 * @description 山洪防治对象表
 * @date 2021-06-08
 */
@Data
public class AttPrevTfBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 地级市
     */
    private String city;

    /**
     * 区县
     */
    private String county;

    /**
     * 街道
     */
    private String town;

    /**
     * 村落
     */
    private String avi;

    /**
     * nvi
     */
    private String nvi;

    /**
     * 区域，平方公里
     */
    private BigDecimal area;

    /**
     * 户数
     */
    private Integer tho;

    /**
     * 人数
     */
    private Integer tpo;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 一般村落、重要村落、防治区域
     */
    private String recType;

    /**
     * risk_type
     */
    private String riskType;

    /**
     * 防汛责任人
     */
    private String chargerName;

    /**
     * 防汛电话
     */
    private String chargerPhone;

    /**
     * lvl
     */
    private String lvl;

    /**
     * 防治区域经纬度坐标点组成的json数组
     */
    private String areaPoints;

    /**
     * 预警状态
     */
    private String isAlarm;

    public AttPrevTfBase() {
    }

}
