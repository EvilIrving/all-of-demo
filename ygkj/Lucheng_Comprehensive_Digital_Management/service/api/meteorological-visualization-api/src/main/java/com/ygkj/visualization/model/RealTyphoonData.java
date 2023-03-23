package com.ygkj.visualization.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 台风历史记录表（从四创获取）
 *
 * @author huangzh 2021-02-01
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
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

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


    /**
     * 登陆强度
     */
    private String landPower;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 风力极值
     */
    private Double windExtremum;

    /**
     * 百分之七十概率风圈
     */
    private String feature;

    public RealTyphoonData() {
    }

}

