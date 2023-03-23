package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("审核详情表")
public class BusDataTableApplyDetail {

    private static final long serialVersionUID = 1L;

    /**
     * 申请id
     */
    @ApiModelProperty("申请id")
    private String applyId;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String applicant;

    /**
     * 操作人id
     */
    @ApiModelProperty("操作人id")
    private String applicantId;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private Date createTime;

    /**
     * 操作
     */
    @ApiModelProperty("操作")
    private String oper;

    /**
     * 意见
     */
    @ApiModelProperty("意见")
    private String remark;

    public BusDataTableApplyDetail() {
    }

    public BusDataTableApplyDetail(String applyId, String applicant, String applicantId, String oper, String remark) {
        this.applyId = applyId;
        this.applicant = applicant;
        this.applicantId = applicantId;
        this.oper = oper;
        this.remark = remark;
    }
}
