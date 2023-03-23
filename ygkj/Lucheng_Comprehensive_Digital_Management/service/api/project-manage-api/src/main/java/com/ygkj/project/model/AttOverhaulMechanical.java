package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-18 13:54
 * @description
 */
@Data
@ApiModel("检修操作-机械操作表")
public class AttOverhaulMechanical implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机械操作id
     */
    @ApiModelProperty("机械操作id")
    private String id;

    @ApiModelProperty("关联工程id")
    private String pid;

    /**
     * 工作负责人
     */
    @ApiModelProperty("工作负责人")
    private String principal;

    /**
     * 工作负责人id
     */
    @ApiModelProperty("工作负责人id")
    private String principalId;

    /**
     * 签发负责人
     */
    @ApiModelProperty("签发负责人")
    private String issueMan;

    /**
     * 签发负责人id
     */
    @ApiModelProperty("签发负责人id")
    private String issueManId;

    /**
     * 签发日期
     */
    @ApiModelProperty("签发日期")
    private Date issueDate;

    /**
     * 签发文件ids 多文件 ， 隔开
     */
    @ApiModelProperty("签发文件ids 多文件 ， 隔开")
    private String fileIds;

    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private Integer delFlag;

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

    @TableField(exist = false)
    private List<SysFile> files;


    public AttOverhaulMechanical() {
    }

}

