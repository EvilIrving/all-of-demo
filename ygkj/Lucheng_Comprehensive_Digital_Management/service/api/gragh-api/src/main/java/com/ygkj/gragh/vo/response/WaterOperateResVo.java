package com.ygkj.gragh.vo.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lxl
 * @create 2021-08-06 15:29
 * @description
 */
@Data
public class WaterOperateResVo {

    /**
     * 水厂编码
     */
    private String waterPlantCode;

    /**
     * 水厂名称
     */
    private String waterPlantName;

    /**
     * 实时流量
     */
    private String supplyFlow;

    /**
     * 余氯是否正常   0异常 1正常
     */
    private Integer clNormal;

    /**
     * 浑浊度是否正常  0异常 1正常
     */
    private Integer ntuNormal;

    /**
     * ph是否正常   0异常 1正常
     */
    private Integer phNormal;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 水厂位置（村）
     */
    private String location;

    /**
     * 供水状态：0 异常；1 正常
     */
    private Integer supplyStatus;

    /**
     * 日累计供水量
     */
    private String totalSupplyQuantity;

    /**
     * PH实时监测值
     */
    private BigDecimal phValue;

    /**
     * 余氯实时监测值
     */
    private BigDecimal clValue;

    /**
     * 浑浊度检测值
     */
    private BigDecimal ntuValue;

    /**
     * 监测时间
     */
    private Date timeRecord;

    /**
     * 是否正常 0-异常 1-正常
     */
    private Integer isNormal;

    /**
     * 设计供水入口
     */
    private BigDecimal desWasuPop;

}
