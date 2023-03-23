package com.ygkj.digitization.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 断面流量表
 * @date 2021-07-31
 */
@Data
public class AttSfBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 断面名称
     */
    private String sectName;

    /**
     * 主测站编码
     */
    private String mainStCode;

    /**
     * 主测站名
     */
    private String mainStName;

    /**
     * 橙色预警下限流量
     */
    private Double orangeFlow;

    /**
     * 红色预警下限流量
     */
    private Double redFlow;

    /**
     * 流域
     */
    private String bas;

    /**
     * 行政区划编码
     */
    private String areaCode;

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

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private boolean delFlag;

    /**
     * 部门id
     */
    private String deptId;

    public AttSfBase() {
    }

}
