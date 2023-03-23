package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PvFloodWarningTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联编码
     */
    private String prevWindexCode;

    /**
     * 准备转移
     */
    private BigDecimal rtransfer;

    /**
     * 立即转移
     */
    private BigDecimal itransfer;

    /**
     * 土壤含水量/前期降雨
     */
    private String soilmstprn;

    /**
     * 临界雨量/水位
     */
    private BigDecimal criticalval;

    /**
     * method
     */
    private String method;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 数据更新时间
     */
    private Date modifyTime;

    /**
     * 区域编码
     */
    private String adcd;

    /**
     * 数据入库时间
     */
    private Date tongTime;

    /**
     * techisenable
     */
    private String techisEnable;

}
