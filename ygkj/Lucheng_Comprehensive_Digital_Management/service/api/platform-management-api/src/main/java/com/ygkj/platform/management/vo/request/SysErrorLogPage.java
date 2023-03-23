package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import lombok.Data;

import java.util.Date;

@Data
public class SysErrorLogPage extends PageEntity {
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

    private String errorDay;
}
