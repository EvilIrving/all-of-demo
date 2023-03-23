package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 风险清单表
 * @date 2021-06-16
 */
@Data
public class BusRiskList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 工程名称
     */
    private String projName;

    /**
     * 工程类别
     */
    private String projType;

    /**
     * 上报人
     */
    private String reportPerson;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String county;

    /**
     * 责任地区行政编码
     */
    private String adcdDuty;

    /**
     * 工程类别：251为隐患点，252为高风险点；253为薄弱点
     */
    private String sbSign;

    /**
     * 上报审核人姓名
     */
    private String checkPerson;

    /**
     * 审核时间
     */
    private Date checkTime;

    /**
     * 风险描述
     */
    private String dangerDes;

    /**
     * 发现渠道（xxjc为县级检查；zczc为工程自查；sjcc为市级抽查；sjdc为省级督查）
     */
    private String property;

    /**
     * 风险类型：防汛准备、工程安全、检测预警、其他
     */
    private String dangerType;

    /**
     * 流域
     */
    private String riverBasin;

    /**
     * 整改情况
     */
    private String recCondition;

    /**
     * 整改责任单位
     */
    private String dutyUnit;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 经度
     */
    private String lng;

    /**
     * 工程id
     */
    private String sbId;

    public BusRiskList() {
    }
}
