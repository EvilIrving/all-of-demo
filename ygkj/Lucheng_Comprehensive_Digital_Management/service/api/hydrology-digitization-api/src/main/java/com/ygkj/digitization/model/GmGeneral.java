package com.ygkj.digitization.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 日常业务管理-日常管理
 *
 * @author xq 2021-02-01
 */
@Data
public class GmGeneral implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务目标
     */
    private String taskTarget;

    /**
     * 存在的问题
     */
    private String existProblem;

    /**
     * 填报单位id
     */
    private String fillDeptId;

    /**
     * 填报单位名称
     */
    private String fillDeptName;

    /**
     * 填报时间
     */
    private Date reportTime;

    /**
     * 任务状态 0为未完成；1为已完成
     */
    private String taskState;

    /**
     * 附件id
     */
    private String attachId;

    /**
     * 行政编码
     */
    private String adcd;

    /**
     * 行政区划
     */
    private String adnm;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer delFlag;

    /**
     * 部门id
     */
    private String deptId;

    public GmGeneral() {
    }

}
