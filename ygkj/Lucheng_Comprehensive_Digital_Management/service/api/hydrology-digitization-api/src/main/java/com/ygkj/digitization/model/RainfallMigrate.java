package com.ygkj.digitization.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 降水迁移表
 *
 * @author xq 2021-02-06
 */
@Data
public class RainfallMigrate implements Serializable {

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
     * 测站名称
     */
    private String stnm;

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

    public RainfallMigrate() {
    }

}
