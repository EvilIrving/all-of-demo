package com.ygkj.platform.management.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LJX
 * @description 登录日志
 * @date 2021-03-23
 */
@Data
public class SysLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 登录的系统
     */
    private String sysCode;

    /**
     * 登录人id
     */
    private String userId;

    /**
     * 登录人名称
     */
    private String userName;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String department;

    /**
     * 登录地ip
     */
    private String loginIp;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /**
     * 登录类型rn1.pcrn2.app
     */
    private String loginType;



    /**
     * 操作内容
     */
    @TableField(exist = false)
    private String operateInfo;

    public SysLoginLog() {
    }

    public SysLoginLog(String userId, String userName, String deptId, String department, String loginIp, Date loginTime) {
        this.userId = userId;
        this.userName = userName;
        this.deptId = deptId;
        this.department = department;
        this.loginIp = loginIp;
        this.loginTime = loginTime;
    }
}
