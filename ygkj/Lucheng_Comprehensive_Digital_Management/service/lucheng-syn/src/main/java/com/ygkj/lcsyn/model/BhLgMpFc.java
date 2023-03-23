package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class BhLgMpFc {

    /**
     * id
     */
    private Integer id;

    /**
     * 汛前检查报告
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
     * 汛后检查报告
     */
    private String afFloodRpt;

    /**
     * 汛后检查完成时间
     */
    private Date afFloodTm;

    /**
     * 白蚁检查报告
     */
    private String termiteCheckRpt;

    /**
     * 白蚁检查完成时间
     */
    private Date termiteCheckTm;

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
