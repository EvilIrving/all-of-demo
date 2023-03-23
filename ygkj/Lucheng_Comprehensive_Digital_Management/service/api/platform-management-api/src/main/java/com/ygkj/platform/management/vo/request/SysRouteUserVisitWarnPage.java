package com.ygkj.platform.management.vo.request;


import com.ygkj.entity.PageEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LJX
 * @description 用户为访问指定页面记录表
 * @date 2021-03-23
 */
@Data
public class SysRouteUserVisitWarnPage extends PageEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 路由id
     */
    private String routeId;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 规则的id
     */
    private Long ruleId;

    /**
     * warn_time
     */
    private Date warnTime;

    /**
     * 发送的记录个数
     */
    private Integer msgCount;

}
