package com.ygkj.project.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 工程风险隐患表
 * @date 2021-06-08
 */
@Data
public class ProjectRisk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 工程id
     */
    private String projectCode;

    /**
     * 工程类型
     */
    private String projectType;

    /**
     * 风险类型
     */
    private String recType;

    /**
     * 隐患类型
     */
    private String riskType;

    /**
     * 发现时间
     */
    private Date discoverTime;

    /**
     * 整改单位
     */
    private String rectifyDeptName;

    /**
     * 整改措施
     */
    private String rectifyMeasure;

    /**
     * 管控措施
     */
    private String controlMeasure;

    /**
     * 整改期限
     */
    private Date rectifyDeadline;

    /**
     * 落实情况
     */
    private String effectStatus;

    /**
     * 隐患地点
     */
    private String riskLocal;

    /**
     * 存在问题
     */
    private String existProblem;

    /**
     * 发现渠道
     */
    private String discoverChannel;

    /**
     * 整改情况
     */
    private String rectifyStatus;

    /**
     * 上报人id
     */
    private String reportUserId;

    /**
     * 主管(监管)
     */
    private String chargerDept;

    /**
     * 上报人姓名
     */
    private String reportUserName;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 流域名称
     */
    private String bas;

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

    public ProjectRisk() {
    }

}
