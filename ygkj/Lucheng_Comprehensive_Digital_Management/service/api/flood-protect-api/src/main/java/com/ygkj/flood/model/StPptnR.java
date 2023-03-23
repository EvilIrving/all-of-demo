package com.ygkj.flood.model;

import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description 实时降雨记录
 * @Date 2021/1/18
 */
@Data
public class StPptnR {

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
     * 时段降水量
     */
    private Double drp;

    /**
     * 时段长
     */
    private Double intv;

    /**
     * 上报时间
     */
    private Date uploadTime;

    private String stationName;

    private boolean warning;

    private double latitude;

    private double longitude;

    private String stType;

    public StPptnR() {
    }
}
