package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;

/**
 *  台风历史记录表（从四创获取）
 * @author huangzh 2021-01-29
 */
@Data
public class RealTyphoonData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;

    /**
     * 台风名称
     */
    private String name;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 登录点坐标
     */
    private String land;

    /**
     * 台风编号
     */
    private String code;

    /**
     * 路径数据
     */
    private String points;

    /**
     * 台风年份
     */
    private String year;

    private String affectedArea;

    private String feature;

    public RealTyphoonData() {
    }

    public RealTyphoonData(String name, String beginTime, String endTime, String land, String code, String points, String year) {
        this.name = name;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.land = land;
        this.code = code;
        this.points = points;
        this.year = year;
    }
}

