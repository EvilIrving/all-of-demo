package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-21 16:00
 * @description
 */
@Data
public class BhLgLmrDisDomSs {

    /**
     * id
     */
    private String id;

    /**
     * 调令编号
     */
    private Integer soId;

    /**
     * 泄洪洞名称
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
     * 开度
     */
    private BigDecimal opening;

    /**
     * 流量
     */
    private BigDecimal flow;

    /**
     * 泄洪洞编号
     */
    private String holeName;


    public BhLgLmrDisDomSs() {
    }

}

