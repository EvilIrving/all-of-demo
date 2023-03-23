package com.ygkj.platform.management.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LJX
 * @description 路由关系分析表
 * @date 2021-03-22
 */
@Data
public class SysRouteRelationAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 路由链，不大于三个路由
     */
    private String routeIdChain;

    /**
     * 路由名称链
     */
    private String routeNameChain;

    /**
     * 路由链出现的次数
     */
    private Long chainCount;

}
