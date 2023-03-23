package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LJX
 * @description 用户查看页面考核表
 * @date 2021-03-23
 */
@Data
public class SysRouteUserVisitRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id集合
     */
    private String userIds;

    /**
     * 路由的id
     */
    private String routeId;

    /**
     * 1.每个工作日一次rn2.每周看一次rn3.每月看一次rn4.每个季度看一次rn5.每年看一次
     */
    private Integer ruleType;

    /**
     * 规则是否开启
     */
    private boolean open;

    /**
     * 通知用户方式
     */
    private String noticeType;

    /**
     * rule_name
     */
    private String ruleName;


}
