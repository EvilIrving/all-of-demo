package com.ygkj.project.vo.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lxl
 * @create 2022-07-05 16:21
 * @description
 */
@Data
public class AttendanceDataResVo {

    /**
     * 姓名
     */
    private String name;

    /**
     * 公司名称
     */
    private String corporationName;

    /**
     * 职务
     */
    private String post;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 签到日期
     */
    private String attendanceTime;

    /**
     * 已签到数量
     */
    private Integer attendanceNum;

    /**
     * 需要签到数量
     */
    private Integer needNum;

    /**
     * 漏签数量
     */
    private Integer missNum;

    /**
     * 请假数量
     */
    private Integer leaveNum;

    /**
     * 考核排名
     */
    private Integer sort;

    /**
     * 考勤到岗率
     */
    private BigDecimal attendanceRate;

}
