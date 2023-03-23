package com.ygkj.lcsyn.model;

import lombok.Data;

/**
 * @description 工程巡查轨迹经纬度记录表
 * @author lxl
 * @date 2022-06-27
 */
@Data
public class BhSlPatrolGisInfo{


    /**
     * 巡查记录编号
     */
    private Integer id;

    /**
     * 工程编号
     */
    private String pjcd;

    /**
     * 轨迹记录（天地图）
     */
    private String position;

    /**
     * 源轨迹记录
     */
    private String positionSource;

    /**
     * 是否巡查完成
     */
    private String isComplete;

    /**
     * upload_position
     */
    private String uploadPosition;

    /**
     * 巡查总长（米）
     */
    private Double gisDistance;

    /**
     * 平均速度（米/秒）
     */
    private Double avgSpeed;

    public BhSlPatrolGisInfo() {
    }

}

