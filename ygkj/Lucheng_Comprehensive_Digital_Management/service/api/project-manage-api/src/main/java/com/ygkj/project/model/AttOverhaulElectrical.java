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
 * @create 2022-04-18 13:53
 * @description
 */
@Data
@ApiModel("检修操作-电气操作表")
public class AttOverhaulElectrical implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电气操作表 id
     */
    @ApiModelProperty("电气操作表 id")
    private String id;

    @ApiModelProperty("关联工程id")
    private String pid;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operator;

    /**
     * 操作人id
     */
    @ApiModelProperty("操作人id")
    private String operatorId;

    /**
     * 监护人
     */
    @ApiModelProperty("监护人")
    private String guardian;

    /**
     * 监护人id
     */
    @ApiModelProperty("监护人id")
    private String guardianId;

    /**
     * 值班领导
     */
    @ApiModelProperty("值班领导")
    private String leader;

    /**
     * 值班领导id
     */
    @ApiModelProperty("值班领导id")
    private String leaderId;

    /**
     * 文件id，多文件用 ， 隔开存储
     */
    @ApiModelProperty("文件id，多文件用 ， 隔开存储")
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

    public AttOverhaulElectrical() {
    }
}
