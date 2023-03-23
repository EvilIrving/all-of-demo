package com.ygkj.data.management.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class BusApiApplyResVo {

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
     * 申请单位
     */
    private String applicantDepartment;

    /**
     * 申请人邮箱
     */
    private String applicantEmail;

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
     * 服务接口名称
     */
    private String apiName;


    /**
     * 接口分类
     */
    private String apiCategory;

    /**
     * 接口类别
     */
    private String apiState;

    private Date createTime;

    private String releaseMethod;

    private String appKey;

    private String appSecret;

}
