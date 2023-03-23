package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class BhDutyRecord {

    /**
     * duty_date
     */
    private Date dutyDate;

    /**
     * pid
     */
    private String pid;

    /**
     * leader
     */
    private String leader;

    /**
     * mgt_day
     */
    private String mgtDay;

    /**
     * mgt_night
     */
    private String mgtNight;

    /**
     * bq_a
     */
    private String bqA;

    /**
     * bq_b
     */
    private String bqB;

    /**
     * bf_a
     */
    private String bfA;

    /**
     * bf_b
     */
    private String bfB;

    /**
     * tm_a
     */
    private String tmA;

    /**
     * tm_b
     */
    private String tmB;

    /**
     * dh_a
     */
    private String dhA;

    /**
     * dh_b
     */
    private String dhB;

    /**
     * cd_a
     */
    private String cdA;

    /**
     * cd_b
     */
    private String cdB;

    /**
     * resume
     */
    private String resume;

    /**
     * week
     */
    private String week;

    /**
     * id
     */
    private Long id;

    /**
     * fs_a
     */
    private String fsA;

    /**
     * fs_b
     */
    private String fsB;

    /**
     * 中层领导
     */
    private String midLeader;

    /**
     * 巡查人员联系电话
     */
    private String mgtNightPhone;

    /**
     * 巡查人员编号
     */
    private String mgtNightId;

    /**
     * 巡查类型
     */
    private String patrolType;

    /**
     * affair
     */
    private String affair;

    /**
     * 白班管理人员
     */
    private String manager;

    /**
     * 晚班管理人员
     */
    private String managerN;

    /**
     * 运行负责
     */
    private Integer runQin;

    /**
     * 值班长
     */
    private Integer dutyCap;

    /**
     * 带班领导（晚班）
     */
    private Integer leaderN;

    /**
     * 运行负责（晚班）
     */
    private Integer runQinN;

    /**
     * 值班长（晚班）
     */
    private Integer dutyCapN;

    /**
     * 带班领导（中班）
     */
    private Integer leaderA;

    /**
     * 运行负责（中班）
     */
    private Integer runQinA;

    /**
     * 值班长（中班）
     */
    private Integer dutyCapA;

    /**
     * 管理值班（中班）
     */
    private String managerA;

    /**
     * 值班员（中班）
     */
    private String mgtAfternoon;

    /**
     * 设备巡查人员
     */
    private String devicePatrolUid;

    /**
     * 物业巡查员
     */
    private Integer propertyPatrolUid;

    /**
     * 值班电话
     */
    private String dutyPhone;
}
