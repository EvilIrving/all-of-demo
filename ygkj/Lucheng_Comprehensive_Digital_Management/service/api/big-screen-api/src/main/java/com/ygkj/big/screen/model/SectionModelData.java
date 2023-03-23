package com.ygkj.big.screen.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SectionModelData implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 戍浦江断面id
     */
    private String sectionid;

    /**
     * 整点时间
     */
    private String hour;

    /**
     * 流量
     */
    private Double flow;

    /**
     * 水深
     */
    private Double depth;
}
