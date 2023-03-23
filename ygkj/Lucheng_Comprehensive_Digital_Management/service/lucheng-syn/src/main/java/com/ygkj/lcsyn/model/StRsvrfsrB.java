package com.ygkj.lcsyn.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StRsvrfsrB {
    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 开始月日
     */
    private String bgmd;

    /**
     * 结束月日
     */
    private String edmd;

    /**
     * 汛限水位
     */
    private BigDecimal fsltdz;

    /**
     * 汛限库容
     */
    private BigDecimal fsltdw;

    /**
     * 汛期类别
     */
    private String fstp;

    /**
     * 时间戳
     */
    private Date moditime;
}
