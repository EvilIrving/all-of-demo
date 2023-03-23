package com.ygkj.digitization.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 日常业务管理-业务管理
 *
 * @author xq 2021-02-01
 */
@Data
public class GmBusiness implements Serializable {

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
     * 执行单位id
     */
    private String exeDeptId;

    /**
     * 执行单位名称
     */
    private String exeDeptName;

    /**
     * 执行人id，用逗号隔开
     */
    private String exeUserId;

    /**
     * 执行人名称，用逗号隔开
     */
    private String exeUserName;

    /**
     * 执行起始时间
     */
    private Date exeStartTime;

    /**
     * 执行结束时间
     */
    private Date exeEndTime;

    /**
     * 任务状态：0：未下达；1：已下达；2:已分配；3：已提醒；4：已超期；5：已完成
     */
    private Integer taskState;

    /**
     * 完成时间
     */
    private Date finishTime;

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

    /**
     * 类别
     */
    private String category;

    /**
     * 进度要求
     */
    private String progress;

    /**
     * 完成情况
     */
    private String completion;

    /**
     * 责任人员
     */
    private String respPerson;

    public GmBusiness() {
    }

}
