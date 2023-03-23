package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 湖泊基础信息表
 *
 * @author huangzh 2021-03-02
 */
@Data
public class AttLkBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 湖泊代码 数据来源：录入
     */
    private String lkCode;

    /**
     * 湖泊名称 数据来源：防洪库、地图平台
     */
    private String lkName;

    /**
     * 左下角经度 单位：°；填写规范：湖泊最小外接矩形左下角 东经度数
     */
    private BigDecimal lowLeftLong;

    /**
     * 左下角维度 单位：°；填写规范：湖泊最小外接矩形左下角 东伟度数
     */
    private BigDecimal lowLeftLat;

    /**
     * 右上角经度 单位：°；填写规范：湖泊最小外接矩形右上角东经度数
     */
    private BigDecimal upRightLong;

    /**
     * 右上角维度 单位：°；填写规范：湖泊最小外接矩形右上角 北纬度数
     */
    private BigDecimal upRightLat;

    /**
     * 跨界类型 数据来源：防洪库；
     */
    private String crOverType;

    /**
     * 湖泊所在位置 填写规范：填写湖泊所在位置，跨国 并跨省和跨国湖泊填外国 国名、我国省名和县名， 如“××国，××省××县、…”； 跨省湖泊填省名和县名， 如“××省××县、××县、 ××(设区市简称)
     */
    private String lkLoc;

    /**
     * 水面面积 单位：km2，；填写规范：湖泊常水位对应的水面面 积。计量单位为km2，记 至2 位小数。如梁子湖多 年平均水面面积为： 228.00km2。
     */
    private BigDecimal watArea;

    /**
     * 湖泊容积 单位：10四次方m3
     */
    private BigDecimal meaAnnLkVol;

    /**
     * 咸淡水属性 数据来源：防洪库；
     */
    private String salFreWat;

    /**
     * 平均水深
     */
    private BigDecimal meaAnnWatDept;

    /**
     * 最大水深
     */
    private BigDecimal maxDept;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录失效时间
     */
    private Date exprDate;

    /**
     * 所属流域
     */
    private String basin;

    /**
     * 河长级别
     */
    private String riverManagerGrade;

    /**
     * 河长姓名
     */
    private String riverManagerName;

    /**
     * 河长电话
     */
    private String riverManagerPhone;

    /**
     * 河长职务
     */
    private String riverManagerJob;

    /**
     * 所属单位
     */
    private String department;

    public AttLkBase() {
    }

}

