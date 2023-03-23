package com.ygkj.flood.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 避灾点信息
 * @date 2021-04-14
 */
@Data
public class HazardPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 避灾点面积
     */
    private Double area;

    /**
     * 避灾点单位
     */
    private String areaUnit;

    /**
     * 避灾点地址
     */
    private String address;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 容纳单位
     */
    private String capacityUnit;

    /**
     * 容纳数量
     */
    private String capacity;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 街道名称
     */
    private String streetName;

    /**
     * 区县名称
     */
    private String areaName;

    /**
     * 避灾点名称
     */
    private String name;

    /**
     * 避灾点编码
     */
    private String placeCode;

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
    private boolean delFlag;

    public HazardPoint() {
    }

}

