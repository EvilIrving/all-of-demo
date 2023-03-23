package com.ygkj.flood.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PvFloodInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预警记录编码
     */
    private String recordCode;

    /**
     * 乡镇行政区划编码
     */
    private String adcd;

    /**
     * 类型:监测预警
     */
    private String warnType;

    /**
     * 预警时间
     */
    private Date warnTime;

    /**
     * 预警级别:5:立即转移6:准备转移
     */
    private Integer warnGrade;

    /**
     * 填写预警值，包括累计雨量
     */
    private BigDecimal waenValue;

    /**
     * 预警来源为雨量时的降雨时段长。单位：h
     */
    private BigDecimal period;

    /**
     * 预警指标
     */
    private BigDecimal windex;

    /**
     * 乡镇名称
     */
    private String townName;

    /**
     * 县（市区）名称
     */
    private String countryName;

    /**
     * 地市名称
     */
    private String cityName;

    /**
     * 防止对象编码
     */
    private String prevCode;

    /**
     * 行政村
     */
    private String adminAdnm;

    /**
     * 自然村
     */
    private String naturalAdnm;

    /**
     * 数据同步时间
     */
    private Date tongTime;

}
