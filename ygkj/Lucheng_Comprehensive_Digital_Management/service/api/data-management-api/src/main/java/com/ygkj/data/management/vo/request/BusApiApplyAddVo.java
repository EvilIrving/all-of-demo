package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusApiApplyAddVo {
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 申请的接口id
     */
    @ApiModelProperty("申请的接口id")
    private String apiId;

    /**
     * 接口申请审核状态
     */
    @ApiModelProperty("接口申请审核状态")
    private String status;

    /**
     * 申请人id
     */
    @ApiModelProperty("申请人id")
    private String applicantId;

    /**
     * 申请人姓名
     */
    @ApiModelProperty("申请人姓名")
    private String applicant;

    /**
     * 申请人电话
     */
    @ApiModelProperty("申请人电话")
    private String applicantMobile;

    /**
     * 申请单位
     */
    @ApiModelProperty("申请单位")
    private String applicantDepartment;

    /**
     * 申请人邮箱
     */
    @ApiModelProperty("申请人邮箱")
    private String applicantEmail;

    /**
     * 申请依据
     */
    @ApiModelProperty("申请依据")
    private String basis;

    /**
     * 申请用途
     */
    @ApiModelProperty("申请用途")
    private String use;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String areaCode;

    /**
     * 流域
     */
    @ApiModelProperty("流域")
    private String basin;

    /**
     * 单日申请最大次数
     */
    @ApiModelProperty("单日申请最大次数")
    private Integer applyNumber;

    /**
     * 申请文件id，关联到系统附件表
     */
    @ApiModelProperty("申请文件id")
    private String applyFileId;
}
