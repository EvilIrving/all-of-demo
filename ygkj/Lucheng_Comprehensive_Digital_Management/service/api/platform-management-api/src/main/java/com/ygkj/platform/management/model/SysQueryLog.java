package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LJX
 * @description 查询接口日志表
 * @date 2021-03-22
 */
@Data
public class SysQueryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 关键词
     */
    private String keyWord;

    /**
     * 来自系统
     */
    private String sysCode;

    /**
     * 系统名称
     */
    private String sysName;

    /**
     * user_id
     */
    private String userId;

    /**
     * user_name
     */
    private String userName;

    /**
     * 来源ip
     */
    private String sourceIp;

    /**
     * 查询时间
     */
    private Date queryTime;

    /**
     * 查询星期
     */
    private String dayWeek;

}
