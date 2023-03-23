package com.ygkj.supervision.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description att_supervision_header
 * @date 2021-05-11
 */
@Data
@ApiModel("att_supervision_header")
public class AttSupervisionHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表单名称
     */
    @ApiModelProperty("表单名称")
    private String formName;

    /**
     * 表单编码
     */
    @ApiModelProperty("表单编码")
    private String formCode;

    /**
     * 检查类型
     */
    @ApiModelProperty("检查类型")
    private String matterType;

    /**
     * 部门编码
     */
    @ApiModelProperty("部门编码")
    private String branchCode;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String branchName;

    /**
     * 表单字段一
     */
    @ApiModelProperty("表单字段一")
    private String fieldOne;

    /**
     * 表单字段二
     */
    @ApiModelProperty("表单字段二")
    private String fieldTwo;

    /**
     * 表单字段三
     */
    @ApiModelProperty("表单字段三")
    private String fieldThree;

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
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private boolean delFlag;

    public AttSupervisionHeader() {
    }

}

