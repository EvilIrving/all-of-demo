package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BhDepJobsettinginfo {

    /**
     * 编号
     */
    @TableId(type = IdType.INPUT)
    private Integer id;

    /**
     * 岗位类别
     */
    private Integer jobtype;

    /**
     * 岗位名称
     */
    private String jobname;

    /**
     * 入职条件
     */
    private String entryrequirement;

    /**
     * 岗位职责
     */
    private String jobduty;

    /**
     * 岗位责任人
     */
    private String jobresponsible;

    /**
     * 在岗人数
     */
    private Integer joucount;

    /**
     * 人员兼岗情况
     */
    private String alsohappening;

    /**
     * 是否物业化
     */
    private String isproperty;

    /**
     * 岗位人员资格证书
     */
    private String personcredentials;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 附件
     */
    private String aid;

    /**
     * 单位编号
     */
    private Integer depId;

    /**
     * pjcd
     */
    private String pjcd;

    /**
     * matters
     */
    private String matters;

    /**
     * 岗位编号
     */
    private String code;

    /**
     * 事项内容
     */
    private String matterscontent;
}
