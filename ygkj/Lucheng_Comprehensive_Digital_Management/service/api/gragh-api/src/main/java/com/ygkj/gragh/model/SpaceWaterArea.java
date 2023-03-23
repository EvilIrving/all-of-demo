package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 水域信息表
 * @date 2020-12-11
 */
@Data
public class SpaceWaterArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 水域名称
     */
    private String name;

    /**
     * 水域面点位
     */
    private String areaPoint;

    /**
     * 中心点纬度
     */
    private String centerLatitude;

    /**
     * 中心点经度
     */
    private String centerLongitude;

    public SpaceWaterArea() {
    }

}
