package com.ygkj.digitization.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Date 2021/2/6
 */
@Data
public class RainfallMigrateResVo {
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

    public RainfallMigrateResVo() {
    }
}
