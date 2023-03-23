package com.ygkj.entity;

import lombok.Data;

@Data
public class DingDingDept {

    /**
     * 主键(部门id)
     */
    private String id;

    /**
     * 父级编码
     */
    private String pid;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer orderId;

    /**
     * 行政区划代码
     */
    private String adcd;

    /**
     * 系统部门编码
     */
    private String sysDept;
}
