package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 水域占用补偿表
 * @date 2021-08-12
 */
@Data
public class AttWaterareaCompensation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 行政区划
     */
    private String areaName;

    /**
     * 占用面积（㎡）
     */
    private BigDecimal occupiedArea;

    /**
     * 补偿面积（㎡）
     */
    private BigDecimal compensationArea;

    /**
     * 删除标识
     */
    private String delFlag;

    public AttWaterareaCompensation() {
    }

}