package com.ygkj.lcsyn.model;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 山洪预警责任人名录表
 * @author
 * @date 2022-05-06
 */
@Data
public class PvPchargeBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 责任人编码
     */
    private String pchargeCode;

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
     * 责任人类型
     */
    private String chargeType;

    /**
     * 责任人级别
     */
    private String chargeLevel;

    /**
     * 对象建立时间
     */
    private Date fromDate;

    /**
     * 对象终止时间
     */
    private Date toDate;

    /**
     * 原始编码
     */
    private String origCode;

    /**
     * 地理信息编码
     */
    private String geoCode;

    /**
     * 同步时间
     */
    private Date tongTime;

    /**
     * 操作标记
     */
    private String op;

    /**
     * 行政区划代码
     */
    private String adcd;

    /**
     * 数据更新时间
     */
    private Date modifyTime;

    /**
     * 是否锁定
     */
    private String isLock;

    /**
     * 审核记录编号
     */
    private String checkId;

    /**
     * 是否通过审核
     */
    private String isDataOfficial;

    /**
     * 审核通过时间
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
     * 删除标记 0未删除 1已删除
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    public PvPchargeBase() {
    }

}
