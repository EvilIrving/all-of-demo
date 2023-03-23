package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StRiverR implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合并测站编码
     */
    private String mgstcd;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 监测时间
     */
    private Date tm;

    /**
     * 水位
     */
    private Double z;

    /**
     * tag
     */
    private boolean tag;

    /**
     * 上报时间
     */
    private Date uploadTime;

    public StRiverR() {
    }
}