package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 灌区基础信息表
 * @date 2021-04-22
 */
@Data
public class AttIrrBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 灌区代码
     */
    private String irrCode;

    /**
     * 灌区名称
     */
    private String irrName;

    /**
     * 左下角经度
     */
    private BigDecimal lowLeftLong;

    /**
     * 左下角维度
     */
    private BigDecimal lowLeftLat;

    /**
     * 右上角经度
     */
    private BigDecimal upLeftLong;

    /**
     * 右上角维度
     */
    private BigDecimal upLeftLat;

    /**
     * 跨界类型
     */
    private String crOverType;

    /**
     * 灌区范围
     */
    private String irrRang;

    /**
     * 主要水源工程类型
     */
    private String majWasoType;

    /**
     * 补充水源工程类型
     */
    private String supWasoType;

    /**
     * 工程规模
     */
    private String engScal;

    /**
     * 设计灌溉面积
     */
    private Integer desIrrArea;

    /**
     * 有效灌溉面积
     */
    private Integer effIrrArea;

    /**
     * 管理单位
     */
    private String manUnit;

    /**
     * 受益范围
     */
    private String benRan;

    /**
     * 渠系水利用系数
     */
    private BigDecimal cwuCoe;

    /**
     * 执行水价
     */
    private BigDecimal ewPrice;

    /**
     * 是否通过标准化管理创建
     */
    private String staMan;

    /**
     * 年度实际灌溉面积(亩)
     */
    private BigDecimal annualActualIrrigationArea;

    /**
     * 骨干渠道长度(千米)
     */
    private BigDecimal mainCanalLength;

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
     * 行政区划
     */
    private String adcd;

    /**
     * 流域
     */
    private String bas;

    /**
     * 行政区划
     */
    private String adnm;

    /**
     * 管理层级
     */
    String managementLevel;

    public AttIrrBase() {
    }

}
