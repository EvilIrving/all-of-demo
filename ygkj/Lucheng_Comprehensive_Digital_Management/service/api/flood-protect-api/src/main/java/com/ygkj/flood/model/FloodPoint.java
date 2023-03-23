package com.ygkj.flood.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 内涝积水点信息
 * @date 2021-04-16
 */
@Data
public class FloodPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 内涝点名称
     */
    private String name;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 水深
     */
    private BigDecimal waterLevel;

    /**
     * 水深单位
     */
    private String waterLevelUnit;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 区县名称
     */
    private String areaName;

    /**
     * 街道名称
     */
    private String streetCode;

    /**
     * 街道名称
     */
    private String streetName;

    /**
     * 获取时间
     */
    private Date acquisitionTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer delFlag;

    private boolean ponding = false;

    /**
     * 测站类型，积水点本来没有测站类型，但是前端地图上不好区分，故加此字段方便区分
     */
    private String stType = "FP";

    public FloodPoint() {
    }

}

