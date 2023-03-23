package com.ygkj.supervision.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description att_supervision_description
 * @date 2021-05-11
 */
@Data
@ApiModel("att_supervision_description")
public class AttSupervisionDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题描述编码
     */
    @ApiModelProperty("问题描述编码")
    private String descriptionCode;

    /**
     * 问题描述名称
     */
    @ApiModelProperty("问题描述名称")
    private String descriptionName;

    /**
     * 上级编码
     */
    @ApiModelProperty("上级编码")
    private String parentCode;


    @ApiModelProperty("所属文件编码")
    private String fileCode;

    /**
     * 问题责任主体
     */
    @ApiModelProperty("问题责任主体")
    private String dutyBody;

    /**
     * 问题分类 包含多个 用，隔开
     */
    @ApiModelProperty("问题分类 包含多个 用，隔开")
    private String problemType;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String formMark;

    /**
     * 相关法律法规
     */
    @ApiModelProperty("相关法律法规")
    private String relateLaw;

    /**
     * 法律法规标准
     */
    @ApiModelProperty("法律法规标准")
    private String lawStandard;

    /**
     * 问题描述
     */
    @ApiModelProperty("问题描述")
    private String problemDescription;

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
    private String delFlag;

    public AttSupervisionDescription() {
    }

}


