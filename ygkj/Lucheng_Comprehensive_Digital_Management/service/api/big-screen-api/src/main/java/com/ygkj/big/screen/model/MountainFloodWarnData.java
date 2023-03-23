package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 山洪灾害防治区重要村落预警数据表
 * @date 2021-06-18
 */
@Data
public class MountainFloodWarnData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测站编码
     */
    private String stationCode;

    /**
     * 时间
     */
    private String tm;

    /**
     * 雨量或水位数据
     */
    private BigDecimal data;

    /**
     * 雨量或水位标识 1:雨量 2:水位
     */
    private String type;

    /**
     * 时间类型
     */
    private BigDecimal period;

    public MountainFloodWarnData() {
    }
}