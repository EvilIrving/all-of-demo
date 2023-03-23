package com.ygkj.flood.vo.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lxl
 * @create 2022-05-20 14:46
 * @description
 */
@Data
public class WarehouseMaterialResVo {

    /**
     * 仓库id
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 物资名称
     */
    private String materialName;

    /**
     * 物资数量
     */
    private Integer num;

    /**
     * 物资单价
     */
    private BigDecimal univalent;

    /**
     * 物资总价
     */
    private BigDecimal amount;

    /**
     * 规格
     */
    private String standard;

}
