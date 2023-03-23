package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DutyManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 值班日期（格式yyyy-mm-dd）r
     */
    private String dutyDate;

    /**
     * 值班时间(格式m月d天)
     */
    private String dutyTime;

    /**
     * 具体星期
     */
    private String week;

    /**
     * 带班领导id
     */
    private String leader;

    /**
     * 带班领导姓名
     */
    private String leaderName;

    /**
     * 白班值班人员id
     */
    private String manager;

    /**
     * 白班值班人员姓名
     */
    private String managerName;

    /**
     * 晚班值班人员id
     */
    private String ngihtManager;

    /**
     * 晚班值班人员姓名
     */
    private String ngihtManagerName;

    /**
     * 当班主任id
     */
    private String dutyChief;

    /**
     * 当班主任名称
     */
    private String dutyChiefName;
}
