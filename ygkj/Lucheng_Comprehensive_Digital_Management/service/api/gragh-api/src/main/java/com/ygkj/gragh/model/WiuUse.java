package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huangzh
 * @description 取水户已用水量
 * @date 2020-12-08
 */
@Data
public class WiuUse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 取水户用水量表
     */
    private String wiuCode;

    /**
     * 累计取水年份
     */
    private String year;

    /**
     * 目前已用水量
     */
    private BigDecimal watUse;

    /**
     * 更新时间
     */
    private Date updateTime;

}

