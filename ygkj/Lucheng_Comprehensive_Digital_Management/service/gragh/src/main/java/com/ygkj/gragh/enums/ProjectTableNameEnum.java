package com.ygkj.gragh.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectTableNameEnum {

    /**
     * 在建工程
     */
    BUILDING("building_project"),

    /**
     * 规划工程
     */
    PROGRAMME("programme_project"),

    /**
     * 计划工程
     */
    PLAN("plan_project"),

    /**
     * 谋划工程
     */
    PLOT("plot_project"),

    /**
     * 前期工程
     */
    EARLY("early_project");

    private String tableName;
}
