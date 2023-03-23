package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huangzh
 * @description 流域基础信息表
 * @date 2020-12-07
 */
@Data
public class AttBasBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流域代码
     */
    private String basCode;

    /**
     * 流域名称
     */
    private String basName;

    /**
     * 左下角经度 单位:°
     */
    private BigDecimal lowLeftLong;

    /**
     * 左下角维度 单位:°
     */
    private BigDecimal lowLeftLat;

    /**
     * 右上角经度 单位:°
     */
    private BigDecimal upRightLong;

    /**
     * 右上角维度 单位:°
     */
    private BigDecimal upRightLat;

    /**
     * 跨界类型 流域的跨界类 型代码，按表 1.2-1 执行
     */
    private String crOverType;

    /**
     * 流域所属行政区
     */
    private String basAdName;

    /**
     * 流域等级 流域等级代码， 按表1.2-2 执行
     */
    private String basGrad;

    /**
     * 流域面积 单位：km2
     */
    private BigDecimal basArea;

    /**
     * 多年平均降水深 单位：mm；又称为多年平 均降水量，可统 计年份降水量 的平均值。计量 单位为mm，记 至1 位小数
     */
    private BigDecimal meaAnnPrec;

    /**
     * 多年平均径流深 单位：mm
     */
    private BigDecimal meaAnnDept;

    /**
     * 多年平均径流量 单位：10的四次方m3；流域出口断面 每年采用多年 平均年径流量，可以使用多年 平均径流深与 流域面积计算 所得。计量单位 为104m3，记至 2 位小数
     */
    private BigDecimal meaAnnRuof;

    /**
     * 流域概况
     */
    private String basOvw;

    /**
     * 干流长度
     */
    private BigDecimal mstrLen;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录失效时间 本条记录初始 时该字段为空， 当有任意一个 字段更新时则 本条记录失效， 本字段填写数 据更新的时间， 更新后的数据 重新生成一条 新记录。
     */
    private Date exprDate;

    public AttBasBase() {
    }

}

