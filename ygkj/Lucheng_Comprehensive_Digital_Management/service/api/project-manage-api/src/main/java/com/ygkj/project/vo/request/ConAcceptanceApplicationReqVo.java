package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ConAcceptanceApplicationReqVo extends PageEntity {
    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("年度")
    private String year;


    @ApiModelProperty("物业单位id")
    private String propertyUnitId;

    @ApiModelProperty("物业单位名称")
    private String propertyUnitName;

    @ApiModelProperty("物业单位发起人id")
    private String propertyUnitReviewerId;


    @ApiModelProperty("运行单位id")
    private String runUnitId;


    @ApiModelProperty("运行单位审核人id")
    private String runUnitReviewerId;



    @ApiModelProperty("水行政主管部门id")
    private String waterAdministrationUnitId;


    @ApiModelProperty("水行政主管部门审核人id")
    private String waterAdministrationUnitReviewerId;


    @ApiModelProperty("合同名称")
    private String contractName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("服务开始期限")
    private Date serviceStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("服务结束期限")
    private Date serviceEndTime;


    @ApiModelProperty("问题描述")
    private String problem;


    @ApiModelProperty("合同附件")
    private String contractFile;


    @ApiModelProperty("验收附件")
    private String acceptanceFile;

    @ApiModelProperty("申请状态 1-审批中 2-已驳回 3-通过")
    private String status;

    @ApiModelProperty("验收类型")
    private String type;

    @ApiModelProperty("当前用户id")
    private String currentUserId;

    @ApiModelProperty("是否是管理员")
    private Boolean isAdmin;

    @ApiModelProperty("是否是物业负责人")
    private Boolean isWyLeader;
}
