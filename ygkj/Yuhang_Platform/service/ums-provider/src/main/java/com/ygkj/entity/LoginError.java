package com.ygkj.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LoginError {

    /**
     * 主键
     */
    private Integer id;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * 电话
     */
    private String phone;

    /**
     * 上次登录失败时间
     */
    private Date lastErrTime;

    /**
     * 当前连续失败次数
     */
    private Integer countNum;

    /**
     * 总共失败次数
     */
    private Integer totalNum;

    /**
     * 用户解锁时间
     */
    private Date unlockTime;

}
