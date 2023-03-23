package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Huangzh
 * @description 堤防基础信息表
 * @date 2020-12-09
 */
@Data
public class AttDikeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 堤防代码
     */
    private String prcd;

    /**
     * 堤防名称
     */
    private String name;

    /**
     * 起点经度
     */
    private BigDecimal dikeStartLongitude;

    /**
     * 起点维度
     */
    private BigDecimal dikeStartLatitude;

    /**
     * 终点经度
     */
    private BigDecimal dikeEndLongitude;

    /**
     * 终点维度
     */
    private BigDecimal dikeEndLatitude;

    /**
     * 堤防中心点经度
     */
    private BigDecimal lgtd;

    /**
     * 堤防中心点维度
     */
    private BigDecimal lttd;


    /**
     * 堤防级别
     */
    private String projectScale;

    /**
     * 堤防形式
     */
    private String dikePattern;

    /**
     * 堤防长度
     */
    private String dikeLength;


    /**
     * 河流岸别
     */
    private String riverBank;


    /**
     * 所在河道等级
     */
    private String riverLevel;


    /**
     * 堤防线
     */
    private String coorpot;

    public AttDikeBase() {
    }

}
