package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PvFloodWarningTargetDirectory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联编码
     */
    private String prevWindexCode;

    /**
     * 行政区划编码
     */
    private String adCode;

    /**
     * 行政村
     */
    private String adminAdnm;

    /**
     * 自然村
     */
    private String naturalAdnm;

    /**
     * 指标类型(雨量、水位)
     */
    private String indextp;

    /**
     * 时段
     */
    private BigDecimal period;

    /**
     * 对象建立时间
     */
    private Date fromDate;

    /**
     * 对象终止时间
     */
    private Date toDate;

    /**
     * tong_time
     */
    private Date tongTime;

    /**
     * 操作标记
     */
    private String op;

    /**
     * 数据更新时间
     */
    private Date modifyTime;

    /**
     * 是否锁定
     */
    private String isLock;

    /**
     * 审查记录编号
     */
    private String checkId;

    /**
     * 是否通过审查
     */
    private String isDataOfficial;

    /**
     * 审查通过时间
     */
    private Date dataOfficialTime;

    /**
     * 审核状态
     */
    private String checkStatus;

    /**
     * 审核原因
     */
    private String checkReason;

    /**
     * techis_enable
     */
    private String techisEnable;
}
