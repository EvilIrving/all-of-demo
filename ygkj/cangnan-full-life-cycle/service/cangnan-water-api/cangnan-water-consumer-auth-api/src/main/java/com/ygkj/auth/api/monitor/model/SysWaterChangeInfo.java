package com.ygkj.auth.api.monitor.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangzh
 * @description 水域变化信息
 * @date 2020-09-10
 */
@Data
public class SysWaterChangeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 水域id
     */
    private String waterId;

    /**
     * 水域名称
     */
    private String waterName;

    /**
     * 行政区划
     */
    private String areaName;

    /**
     * 水域类型
     */
    private String waterType;

    /**
     * 年度
     */
    private String waterYear;

    /**
     * 水域面积（单位：m²）
     */
    private Double waterArea;

    /**
     * 库容量（单位：m³）
     */
    private Double capacity;

    /**
     * 河底高程（m）
     */
    private Double waterHeight;

    /**
     * 实际岸线（m）
     */
    private Double shoreline;

    /**
     * 点位范围集
     */
    private String linePoint;

    /**
     * 临水线点位集
     */
    private String waterfrontLinePoint;

    /**
     * 水面线点位集
     */
    private String surfaceLinePoint;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    /**
     * 是否删除(1未删除；0已删除)
     */
    private Integer isDeleted;

}
