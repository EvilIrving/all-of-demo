package com.ygkj.lcsyn.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AttTideFcstValBase {

    /**
     * 预报id
     */
    private String fcstId;

    /**
     * 潮位站编码
     */
    private String stCode;

    /**
     * 时刻
     */
    private Date tm;

    /**
     * 潮位
     */
    private BigDecimal tdz;

    /**
     * 是否是自动预报最高潮位值0否1是
     */
    private int autoMax;
}
