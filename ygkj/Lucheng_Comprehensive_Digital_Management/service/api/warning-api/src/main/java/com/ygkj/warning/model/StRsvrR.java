package com.ygkj.warning.model;

import lombok.Data;

import java.util.Date;

@Data
public class StRsvrR {
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
    private Double rz;

    /**
     * tag
     */
    private boolean tag;

    /**
     * 上报时间
     */
    private Date uploadTime;


    public StRsvrR() {
    }

}
