package com.ygkj.gragh.model;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 降水历史极值
 *
 * @author 大狼狗 2021-02-23
 */
@Data
public class ExtremRainfall implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 水文监测站代码
     */
    private String stCode;

    /**
     * 水文监测站名称
     */
    private String stName;

    /**
     * 测量时间
     */
    private Date tm;

    /**
     * 时段降水量
     */
    private Double drp;

    /**
     * 时段类型，m:分钟;d:天;h:小时
     */
    private String drcd;

    /**
     * 水文监测站经度
     */
    private BigDecimal stLong;

    /**
     * 水文监测站维度
     */
    private BigDecimal stLat;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 流域
     */
    private String bas;

    public ExtremRainfall() {
    }

}
