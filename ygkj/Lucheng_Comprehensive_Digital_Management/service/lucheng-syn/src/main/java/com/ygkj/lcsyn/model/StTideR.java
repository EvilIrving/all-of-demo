package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class StTideR {

    /**
     * 合并测站编码
     */
    private String mgstcd;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 检测时间
     */
    private Date tm;

    /**
     * 潮位
     */
    private Double tdz;

    public void setStcd(String stcd){
        this.stcd = stcd.trim();
        this.mgstcd = this.stcd;
    }
}
