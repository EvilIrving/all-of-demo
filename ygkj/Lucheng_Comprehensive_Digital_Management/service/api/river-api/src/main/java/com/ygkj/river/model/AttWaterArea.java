package com.ygkj.river.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 水域面积表
 * @date 2021-08-12
 */
@Data
public class AttWaterArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 水域面积
     */
    private BigDecimal waterArea;

    /**
     * 行政区划
     */
    private String areaName;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 类型 1:河道水域面 2:其他水域水域面 3:人工水道水域面 4:山塘水域面 5:水库水域面
     */
    private String type;

    public AttWaterArea() {
    }

}