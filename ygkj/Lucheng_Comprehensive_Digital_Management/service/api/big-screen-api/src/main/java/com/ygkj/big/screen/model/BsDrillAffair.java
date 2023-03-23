package com.ygkj.big.screen.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description bs_drill_affair
 * @date 2021-07-10
 */
@Data
public class BsDrillAffair implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 演练事务唯一id
     */
    private String drillAffairId;

    /**
     * 演练方案
     */
    private String drillCaseId;

    /**
     * 演练开始时刻
     */
    private Date beginTime;

    /**
     * 演练状态，0代表一开始，1代表已关闭
     */
    private Integer drillState;

    public BsDrillAffair() {
    }

}