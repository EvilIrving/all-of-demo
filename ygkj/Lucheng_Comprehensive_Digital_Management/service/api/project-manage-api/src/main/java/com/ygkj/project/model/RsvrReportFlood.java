package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @description 水库报汛表
 * @date 2021-07-22
 */
@Data
public class RsvrReportFlood implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public RsvrReportFlood() {
    }

}
