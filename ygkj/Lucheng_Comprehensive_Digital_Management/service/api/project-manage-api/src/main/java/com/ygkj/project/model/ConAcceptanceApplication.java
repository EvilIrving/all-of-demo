package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("合同管理验收申请")
public class ConAcceptanceApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("年度")
    private String year;


    @ApiModelProperty("物业单位id")
    private String propertyUnitId;


    @TableField(exist = false)
    private String propertyUnitName;


    @ApiModelProperty("物业单位发起人id")
    private String propertyUnitReviewerId;


    @TableField(exist = false)
    private String propertyUnitReviewerName;


    @ApiModelProperty("运行单位id")
    private String runUnitId;


    @TableField(exist = false)
    private String runUnitName;


    @ApiModelProperty("运行单位审核人id")
    private String runUnitReviewerId;

    @TableField(exist = false)
    private String runUnitReviewerName;


    @ApiModelProperty("水行政主管部门id")
    private String waterAdministrationUnitId;


    @TableField(exist = false)
    private String waterAdministrationUnitName;


    @ApiModelProperty("水行政主管部门审核人id")
    private String waterAdministrationUnitReviewerId;


    @TableField(exist = false)
    private String waterAdministrationUnitReviewerName;


    @ApiModelProperty("合同名称")
    private String contractName;


    @ApiModelProperty("服务开始期限")
    private Date serviceStartTime;


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


    @ApiModelProperty("当前节点")
    private Integer currentnode;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;


    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("创建人")
    private String createId;


    @ApiModelProperty("修改时间")
    private Date updateTime;


    @ApiModelProperty("修改人")
    private String updateId;

    @TableField(exist = false)
    private List<SysFile> contractFiles;

    @TableField(exist = false)
    private List<SysFile> acceptanceFiles;

    @TableField(exist = false)
    private ConAcceptanceApplicationNode currentNode;

    @TableField(exist = false)
    private String currentPeople;

    @TableField(exist = false)
    private String currentPeopleId;
}
