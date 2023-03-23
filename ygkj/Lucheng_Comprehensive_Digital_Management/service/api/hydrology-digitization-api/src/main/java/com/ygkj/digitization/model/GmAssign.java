package com.ygkj.digitization.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;

/**
 * 日常业务管理-任务书下达
 *
 * @author xq 2021-02-01
 */
@Data
public class GmAssign implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 任务要求
     */
    private String taskRequest;

    /**
     * 计划完成次数
     */
    private String planFinishTimes;

    /**
     * 实际完成次数
     */
    private String realFinishTimes;

    /**
     * 执行单位id
     */
    private String exeDeptId;

    /**
     * 执行单位名称
     */
    private String exeDeptName;

    /**
     * 执行起始时间
     */
    private Date exeStartTime;

    /**
     * 执行结束时间
     */
    private Date exeEndTime;

    /**
     * 任务状态：0：未下达；1：已下达；2：已提醒；3：已超期；4：已完成
     */
    private Integer taskState;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 执行单位所属行政编码
     */
    private String adcd;

    /**
     * 执行单位所属行政区划
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

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件附件
     */
    private String fileUrl;

    /**
     * 上传人姓名
     */
    private String uploadName;

    public GmAssign() {
    }

}

