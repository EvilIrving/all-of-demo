package com.ygkj.gragh.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lxl
 * @create 2022-04-11 17:30
 * @description
 */
@Data
public class TorrentDeviceData {


    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * json格式字符串
     */
    private String timingData;

    private String reportTime;
    private String createTime;
    private Integer online;

    /**
     * 遥测站地址/设备编码
     */
    private String addr;

    /**
     * 数据采集时间
     */
    private String dataTime;

    /**
     * 水位
     */
    private BigDecimal z;

    /**
     * 预警状态，0未报警，1黄色报警 2红色报警
     */
    private Integer fml;

    /**
     * 电压值
     */
    private BigDecimal vt;

    /**
     * 当日降雨量。每日早晨8点开始累计
     */
    private BigDecimal pj;

    /**
     * 5分钟降雨量
     */
    private BigDecimal pn05;

    /**
     * 累计降雨量。某些情况（雨量筒满）时会重新累计。
     */
    private BigDecimal pt;

    /**
     * 信号强度。大于-75信号会差
     */
    private Integer csq;

    /**
     * 拾音反馈等级
     */
    private Integer pickupStatus;

    private BigDecimal rainfallHour1;
    private BigDecimal rainfallHour3;
    private BigDecimal rainfallHour6;

    public TorrentDeviceData() {
    }

}
