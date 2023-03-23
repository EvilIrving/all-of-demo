package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-21 15:59
 * @description
 */
@Data
public class BhLgLmrDisDomGs {

    /**
     * id
     */
    private String id;

    /**
     * 调令编号
     */
    private Integer soId;

    /**
     * 闸门名称
     */
    private String name;

    /**
     * 实际开启时间
     */
    private Date actlOpenTm;

    /**
     * 实际关闭时间
     */
    private Date actlCloseTm;

    /**
     * 状态报告时间
     */
    private Date reportTm;

    /**
     * 开度
     */
    private BigDecimal opening;

    /**
     * 流量
     */
    private BigDecimal flow;

    /**
     * 启闭照片
     */
    private String photo;

    /**
     * 工程编号
     */
    private String pjcd;

    /**
     * 闸门编号
     */
    private String gateCode;


    public BhLgLmrDisDomGs() {
    }

}

