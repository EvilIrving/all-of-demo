package com.ygkj.flood.model;

import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/4/25
 */
@Data
public class StWasR {

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
     * 检测时间
     */
    private Date tm;

    /**
     * 闸上水位
     */
    private Double upz;

    /**
     * 闸下水位
     */
    private Double dwz;

    /**
     * 最大过闸流量
     */
    private String mxgtq;

    /**
     * 开度
     */
    private String ovs;

    /**
     * tgtq
     */
    private Double tgtq;

    /**
     * swchrcd
     */
    private String swchrcd;

    /**
     * supwptn
     */
    private String supwptn;

    /**
     * sdwwptn
     */
    private String sdwwptn;

    /**
     * msqmt
     */
    private String msqmt;

    /**
     * modit
     */
    private Date modit;

    /**
     * tag
     */
    private boolean tag;

    /**
     * upload_time
     */
    private Date uploadTime;

    public StWasR() {
    }
}
