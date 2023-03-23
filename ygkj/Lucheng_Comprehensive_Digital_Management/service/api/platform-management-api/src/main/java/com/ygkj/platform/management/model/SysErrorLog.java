package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 访问的ip地址
     */
    private String errorIp;

    /**
     * 系统错误类型rnerror_visit异常访问rnerror_url异常urlrnerror_query异常查询rnerror_api接口报错rnerror_time接口熔断错误
     */
    private String errorType;

    /**
     * error_name
     */
    private String errorName;

    /**
     * error_info
     */
    private String errorInfo;

    /**
     * error_time
     */
    private Date errorTime;

    /**
     * 错误的
     */
    private String errorDay;

}
