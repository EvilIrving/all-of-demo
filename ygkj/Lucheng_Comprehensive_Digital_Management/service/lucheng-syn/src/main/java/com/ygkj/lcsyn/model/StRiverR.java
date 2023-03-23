package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class StRiverR {

    private String mgstcd;

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
    private int tag;

    public void setStcd(String stcd){
        this.stcd = stcd.trim();
        this.mgstcd = this.stcd;
    }
}
