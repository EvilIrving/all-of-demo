package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RsvrReportFlood {

    @TableId
    /**
     * 本系统水库编码
     */
    private String resCode;

    /**
     * 源数据水库名称
     */
    private String originResName;

    /**
     * 报汛时刻
     */
    private Date tm;

    /**
     * 入库 流量
     */
    private BigDecimal inflow;

    /**
     * 出库 流量
     */
    private BigDecimal outflow;

    /**
     * 水位
     */
    private BigDecimal waterLevel;
}
