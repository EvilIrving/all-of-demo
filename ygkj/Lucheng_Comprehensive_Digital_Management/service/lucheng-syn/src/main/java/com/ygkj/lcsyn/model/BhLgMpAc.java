package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class BhLgMpAc {

    /**
     * id
     */
    private Integer id;

    /**
     * 年度检查报告
     */
    private String aid;

    /**
     * 工程编号
     */
    private String pid;

    /**
     * 检查时间
     */
    private Date tm;

    /**
     * 带队检查人
     */
    private String checkUid;

    /**
     * 参加人员
     */
    private String joinUid;

    /**
     * 隐患数量
     */
    private Integer hidCount;

    /**
     * 检查照片
     */
    private String checkPic;
}
