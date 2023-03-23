package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 演练方案表
 * @date 2021-07-09
 */
@Data
public class BsDrillCase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 演练方案id
     */
    private String id;

    /**
     * 方案名称
     */
    private String caseName;

    /**
     * 组件id
     */
    private String unitId;

    /**
     * 组件名称
     */
    private String unitName;

    /**
     * 方案类型，表示该组件下的哪种方案类型
     */
    private String caseType;

    /**
     * 演练状态
     */
    private Integer drillState;

    /**
     * 方案时长，单位小时
     */
    private Integer hourNum;

    /**
     * 原始数据起始时刻
     */
    private Date startTime;

    /**
     * 原始数据截止时刻
     */
    private Date endTime;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 方案描述
     */
    private String caseDescription;

    /**
     * 方案描述
     */
    private String caseRelId;

    public BsDrillCase() {
    }

}
