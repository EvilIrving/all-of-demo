package com.ygkj.supervision.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description att_supervision_file
 * @date 2021-05-11
 */
@Data
@ApiModel("att_supervision_file")
public class AttSupervisionFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件编码
     */
    @ApiModelProperty("文件编码")
    private String fileCode;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String fileUrl;

    @ApiModelProperty("部门编码")
    private String branchCode;

    @ApiModelProperty("部门名称")
    private String branchName;

    @ApiModelProperty("检查事项")
    private String matterType;

    @ApiModelProperty("文件类型")
    private String fileType;

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

    public AttSupervisionFile() {
    }

}
