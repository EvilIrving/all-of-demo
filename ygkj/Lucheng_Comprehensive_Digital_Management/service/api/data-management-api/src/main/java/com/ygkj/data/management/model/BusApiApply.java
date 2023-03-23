package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * api接口申请记录
 *
 * @author huang.zh 2021-03-17
 */
@Data
public class BusApiApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 申请的接口id
     */
    private String apiId;

    /**
     * 接口申请审核状态
     */
    private String status;

    /**
     * 申请人id
     */
    private String applicantId;

    /**
     * 申请人姓名
     */
    private String applicant;

    /**
     * 申请人电话
     */
    private String applicantMobile;

    /**
     * 申请人邮箱
     */
    private String applicantEmail;

    /**
     * 申请单位
     */
    private String applicantDepartment;

    /**
     * 申请依据
     */
    private String basis;

    /**
     * 申请用途
     */
    private String use;

    /**
     * 行政区划
     */
    private String areaCode;

    /**
     * 流域
     */
    private String basin;

    /**
     * 单日申请最大次数
     */
    private Integer applyNumber;

    /**
     * 申请文件id，关联到系统附件表
     */
    private String applyFileId;

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
    private boolean delFlag;

    public BusApiApply() {
    }

}

