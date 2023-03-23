package com.ygkj.platform.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author LJX
 * @description 路由日志表
 * @date 2021-03-22
 */
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysRouteLog implements Serializable {

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

    private String deptId;

    private String department;

    /**
     * 来源ip
     */
    private String sourceIp;

    /**
     * 查询时间
     */
    private Date routeTime;

    /**
     * 查询日期
     */
    private String routeDate;

    /**
     * 查询星期
     */
    private String routeUrl;
    /**
     * 路由名称
     */
    private String routeName;

    private String routeId;

    private String interviewMethod;

    private String permissionId;

    private SysPermissionRoute trace;

    @Setter(AccessLevel.NONE)
    private List<String> traceRoute;

    private Integer num;

    public void setTrace(SysPermissionRoute trace) {
        this.trace = trace;
        List<String> traceRouteList = new ArrayList<>();
        SysPermissionRoute route = trace;
        while (route != null) {
            traceRouteList.add(route.getName());
            route = route.getParent();
        }
        Collections.reverse(traceRouteList);
        this.traceRoute = traceRouteList;
    }
}
