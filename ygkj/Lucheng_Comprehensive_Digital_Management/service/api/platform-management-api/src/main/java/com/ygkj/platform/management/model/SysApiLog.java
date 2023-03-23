package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LJX
 * @description 接口调用日志表
 * @date 2021-03-22
 */
@Data
public class SysApiLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

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
     * 部门id
     */
    private String deptId;

    /**
     * 部门名称
     */
    private String department;

    /**
     * api名称
     */
    private String apiName;

    /**
     * api的标识
     */
    private String apiCode;

    private String sourceIp;

    /**
     * 响应时间，毫秒
     */
    private Long responseTime;

    /**
     * 请求时间
     */
    private Date requestTime;

    private String parameters;

    private Integer code;

    private String responseStatus;

    public SysApiLog() {
    }

    public SysApiLog(String sysCode, String sysName, String userId, String userName, String deptId, String department,
                     String apiName, String apiCode, String sourceIp, Long responseTime,
                     Date requestTime, String parameters) {
        this.sysCode = sysCode;
        this.sysName = sysName;
        this.userId = userId;
        this.userName = userName;
        this.deptId = deptId;
        this.department = department;
        this.apiName = apiName;
        this.apiCode = apiCode;
        this.sourceIp = sourceIp;
        this.responseTime = responseTime;
        this.requestTime = requestTime;
        this.parameters = parameters;
    }
}
