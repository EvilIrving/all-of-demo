package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 防御对象名录表
 * @author
 * @date 2022-05-10
 */
@Data
public class PvPrevObjBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 防治对象编码
     */
    private String prevCode;

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
     * 已有或新增
     */
    private String isAdded;

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
     * 数据更新时间
     */
    private Date modifyTime;

    /**
     * 行政区划
     */
    private String adcd;

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

    public PvPrevObjBase() {
    }

}
