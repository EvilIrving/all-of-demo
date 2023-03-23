package com.ygkj.auth.api.monitor.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fml
 * @description 水域基本信息
 * @date 2020-09-07
 */
@Data
@ToString
public class SysWaterBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 水域名称
     */
    private String waterName;

    /**
     * 行政区划
     */
    private String areaName;

    /**
     * 水域面积
     */
    private Double waterArea;

    /**
     * 库容量（单位：m³）
     */
    private Double capacity;

    /**
     * 实际岸线（m）
     */
    private Double shoreline;

    /**
     * 治导线（m）
     */
    private Double ruleWire;

    /**
     * 管理范围线（m）
     */
    private Double scopeLine;

    /**
     * 河底高程
     */
    private Double waterHeight;

    /**
     * 水域类型
     */
    private String waterType;

    /**
     * 水质
     */
    private String waterQuality;

    /**
     * 管理单位
     */
    private String managementUnit;

    /**
     * 开发率
     */
    private Double developRate;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 水体用途
     */
    private String waterUse;

    /**
     * 岸线信息
     */
    private String shorelineInfo;

    /**
     * 范围线类型(水面线、临水线、范围线)
     */
    private String scopeLineType;

    /**
     * 地图范围线文件路径
     */
    private String filePath;

    /**
     * 地图范围线文件名
     */
    private String fileName;

    /**
     * 范围线点位范围集
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
     * 工程规模
     */
    private String grade;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    /**
     * is_deleted
     */
    private Integer isDeleted;

}
