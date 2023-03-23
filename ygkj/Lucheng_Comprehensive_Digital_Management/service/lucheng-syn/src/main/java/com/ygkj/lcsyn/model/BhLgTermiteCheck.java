package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class BhLgTermiteCheck {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 工程编号
     */
    private String pid;

    /**
     * 检查报告
     */
    private String report;

    /**
     * 检查时间
     */
    private Date tm;
}
