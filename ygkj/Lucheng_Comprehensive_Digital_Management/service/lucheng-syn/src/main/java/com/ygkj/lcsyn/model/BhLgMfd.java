package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BhLgMfd {

    @TableId(type = IdType.INPUT)
    /**
     * id
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 维养资金申报文件
     */
    private String aid;

    /**
     * 年度维修养护申报资金
     */
    private BigDecimal dclrFunds;

    /**
     * 工程编号
     */
    private String pid;

    /**
     * 年度维修养护计划文件
     */
    private String mtnPlan;

    /**
     * 上级下达的年度维修养护资金（万元）
     */
    private BigDecimal superiorfunds;

    /**
     * 管理单位自筹的年度维修养护资金（万元）
     */
    private BigDecimal selffunds;

    /**
     * 已使用的维修养护资金
     */
    private BigDecimal usedamount;

    /**
     * 已支付的维修养护资金
     */
    private BigDecimal paidamount;
}
