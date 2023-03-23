package com.ygkj.lcsyn.model;

import lombok.Data;

/**
 * @description 工程巡查频次表
 * @author
 * @date 2022-04-25
 */
@Data
public class BhSlProjPatrolRate {

    /**
     * 编号
     */
    private Integer id;

    /**
     * pjcd
     */
    private String pjcd;

    /**
     * 频次
     */
    private Integer rate;

    /**
     * 类型（1：日，2：周，3：月，4：年）
     */
    private Integer rateType;

    /**
     * 开始日期
     */
    private String beginTm;

    /**
     * 结束日期
     */
    private String endTm;

    /**
     * 巡查周期
     */
    private Integer cycle;

    /**
     * 提醒时间
     */
    private String remindTime;

    /**
     * 周期类型(1：汛期 2：非汛期)
     */
    private Integer cycleType;

    public BhSlProjPatrolRate() {
    }

}

