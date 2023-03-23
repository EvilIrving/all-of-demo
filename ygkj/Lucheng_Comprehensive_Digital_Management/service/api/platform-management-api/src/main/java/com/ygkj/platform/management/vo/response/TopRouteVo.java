package com.ygkj.platform.management.vo.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class TopRouteVo implements Serializable {

    /**
     * 来自系统
     */
    private String sysCode;

    /**
     * 系统名称
     */
    private String sysName;

    /**
     * user_name
     */
    private String userName;

    /**
     * 路由名称
     */
    private String routeName;


    private String routeId;

    /**
     * 访问次数
     */
    private Integer routeNum;
}
