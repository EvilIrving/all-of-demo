package com.ygkj.supervision.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description att_supervision_project
 * @date 2021-05-08
 */
@Data
@ApiModel("att_supervision_project")
@TableName("att_supervision_project")
public class AttSupervisionProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    @TableId(value = "project_code")
    private String projectCode;

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 项目内容
     */
    @ApiModelProperty("项目内容")
    private String projectContent;

    @ApiModelProperty("项目分类  例如： 规划处-水利资金监督检查")
    private String projectType;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

    /**
     * 行政区划名称
     */
    @ApiModelProperty("行政区划名称")
    private String areaName;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String chargePerson;

    /**
     * 负责人电话
     */
    @ApiModelProperty("负责人电话")
    private String chargePhone;

    /**
     * 责任单位
     */
    @ApiModelProperty("责任单位")
    private String chargeUnit;

    /**
     * 业主联系人
     */
    @ApiModelProperty("业主联系人")
    private String ownerPerson;

    /**
     * 业主联系电话
     */
    @ApiModelProperty("业主联系电话")
    private String ownerPhone;

    /**
     * 业主单位
     */
    @ApiModelProperty("业主单位")
    private String ownerUnit;

    /**
     * 检测单位
     */
    @ApiModelProperty("检测单位")
    private String monitorUnit;

    /**
     * 检测联系人
     */
    @ApiModelProperty("检测联系人")
    private String monitorPerson;

    /**
     * 检测联系电话
     */
    @ApiModelProperty("检测联系电话")
    private String monitorPhone;

    /**
     * 水行政主管单位
     */
    @ApiModelProperty("水行政主管单位")
    private String waterUnit;

    /**
     * 用水户
     */
    @ApiModelProperty("用水户")
    private String waterUser;

    /**
     * 监理单位
     */
    @ApiModelProperty("监理单位")
    private String supervisorUnit;

    /**
     * 站点名称
     */
    @ApiModelProperty("站点名称")
    private String siteName;

    /**
     * 主管单位
     */
    @ApiModelProperty("主管单位")
    private String managerUnit;

    /**
     * 批复文号
     */
    @ApiModelProperty("批复文号")
    private String approvalNum;


    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String beginDate;

    /**
     * 法人联系人
     */
    @ApiModelProperty("法人联系人")
    private String legalPerson;

    /**
     * 法人单位
     */
    @ApiModelProperty("法人单位")
    private String legalEntity;

    /**
     * 施工单位
     */
    @ApiModelProperty("施工单位")
    private String constructionUnit;

    /**
     * 施工联系人
     */
    @ApiModelProperty("施工联系人")
    private String constructionPerson;

    /**
     * 监管单位
     */
    @ApiModelProperty("监管单位")
    private String supervisoryUnit;

    /**
     * 监管联系人
     */
    @ApiModelProperty("监管联系人")
    private String supervisoryPerson;

    /**
     * 设计单位
     */
    @ApiModelProperty("设计单位")
    private String designUnit;

    /**
     * 设计联系人
     */
    @ApiModelProperty("设计联系人")
    private String designPerson;

    /**
     * 投资金额
     */
    @ApiModelProperty("投资金额")
    private String investedAmount;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private String delFlag;

    @ApiModelProperty("建设单位")
    private String buildUnit;

    @ApiModelProperty("建设联系电话")
    private String buildPhone;

    @ApiModelProperty("建设单位联系人")
    private String buildPerson;

    @ApiModelProperty("施工联系人")
    private String constructionPhone;

    @ApiModelProperty("监管联系电话")
    private String supervisoryPhone;

    @ApiModelProperty("工程类别")
    private String type;


    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;


    public AttSupervisionProject() {
    }

}
