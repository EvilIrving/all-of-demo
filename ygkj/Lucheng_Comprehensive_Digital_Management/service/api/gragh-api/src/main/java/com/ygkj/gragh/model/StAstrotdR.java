package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 天文潮预报成果表
 * @date 2021-05-18
 */
@Data
public class StAstrotdR implements Serializable {

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
     * 发生时间
     */
    private Date ymdh;

    /**
     * 预报天文潮位
     */
    private Double tdz;

    /**
     * 测站名称
     */
    private String stnm;

    /**
     * tag
     */
    private boolean tag;

    /**
     * savetime
     */
    private Date savetime;

    /**
     * moditime
     */
    private Date moditime;

    public StAstrotdR() {
    }

}
