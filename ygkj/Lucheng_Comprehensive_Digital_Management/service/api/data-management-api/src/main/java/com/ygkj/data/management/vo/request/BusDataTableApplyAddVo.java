package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BusDataTableApplyAddVo {
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 申请的数据资源id
     */
    @ApiModelProperty("申请的数据资源id")
    private String tableId;

    /**
     * 申请人id
     */
    @ApiModelProperty("申请的数据资源id")
    private String applicantId;

    /**
     * 申请人姓名
     */
    @ApiModelProperty("申请的数据资源id")
    private String applicant;

    @ApiModelProperty("前端忽略")
    private String status;

    /**
     * 申请人电话
     */
    @ApiModelProperty("申请人电话")
    private String applicantMobile;

    /**
     * 申请人邮箱
     */
    @ApiModelProperty("申请人邮箱")
    private String applicantEmail;

    /**
     * 申请单位
     */
    @ApiModelProperty("申请单位")
    private String applicantDepartment;

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
