package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 接口申请表
 * @author wangd
 * @date 2020-09-24
 */
@Data
@ApiModel("接口申请表")
public class BusApiApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 接口id
     */
    @ApiModelProperty("接口id")
    private String apiId;

    /**
     * 联系人姓名
     */
    @ApiModelProperty("联系人姓名")
    private String contactMan;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String contactPhone;

    /**
     * 联系人邮箱
     */
    @ApiModelProperty("联系人邮箱")
    private String contactMail;

    /**
     * 申请依据
     */
    @ApiModelProperty("申请依据")
    private String applyReason;

    /**
     * 系统名称
     */
    @ApiModelProperty("系统名称")
    private String applySystem;

    /**
     * 申请状态 0-待审核 1-审核通过 2-驳回
     */
    @ApiModelProperty("申请状态 0-待审核 1-审核通过 2-驳回")
    private Integer applyStatus;

    @ApiModelProperty("接口类型 1-服务接口 2-数据接口")
    private Integer apiType ;

    /**
     * 审核人
     */
    @ApiModelProperty("审核人")
    private String applyMan;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date applyTime;

    /**
     *  审核意见
     */
    @ApiModelProperty("审核意见")
    private String applyRemark;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty("删除标记")
    private Boolean deleted;

    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    private String apiName;

    /**
     * 接口分类id
     */
    @ApiModelProperty("接口分类id")
    private String categoryId;

    /**
     * 一级目录
     */
    @ApiModelProperty("一级目录名称")
    private String categoryFst;

    /**
     * 二级目录
     */
    @ApiModelProperty("二级目录名称")
    private String categorySec;

    /**
     * 申请机构
     */
    @ApiModelProperty("申请机构")
    private String applyOrg;

    public BusApiApply() {
    }
}