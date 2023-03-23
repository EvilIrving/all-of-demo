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
 * @create 2022-04-18 17:59
 * @description
 */
@Data
@ApiModel("维修养护日常任务")
public class MtDailyTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("工程id")
    private String prcd;

    /**
     * 工程类型
     */
    @ApiModelProperty("工程类型")
    private String projectType;

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 一般问题、较重问题、严重问题
     */
    @ApiModelProperty("一般问题、较重问题、严重问题")
    private String problemType;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    private Date tm;

    /**
     * 任务描述
     */
    @ApiModelProperty("任务描述")
    private String taskDescribe;

    /**
     * 具体隐患
     */
    @ApiModelProperty("具体隐患")
    private String hiddenDanger;

    /**
     * 隐患图片
     */
    @ApiModelProperty("隐患图片")
    private String imgIds;

    /**
     * 处理方式
     */
    @ApiModelProperty("处理方式")
    private String processWay;

    /**
     * 计划完成时间
     */
    @ApiModelProperty("计划完成时间")
    private Date planTm;

    /**
     * 问题来源
     */
    @ApiModelProperty("问题来源")
    private String problemSource;

    /**
     * 实施单位
     */
    @ApiModelProperty("实施单位")
    private String implementUnit;

    /**
     * 实施人员
     */
    @ApiModelProperty("实施人员")
    private String implementUser;

    /**
     * 实施人员id
     */
    @ApiModelProperty("实施人员id")
    private String implementUserId;

    @ApiModelProperty("上报人")
    private String creator;

    /**
     * 指导意见
     */
    @ApiModelProperty("指导意见")
    private String instructions;

    @TableField(exist = false)
    private List<SysFile> images;

    /**
     * 年份
     */
    @TableField(exist = false)
    private Integer year;

    /**
     * 隐患状态（1-已处理、2-待处理、3-处理中）
     */
    @ApiModelProperty("隐患状态（1-已处理、2-待处理、3-处理中）")
    private Integer hiddenDangerStatus;

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

    public MtDailyTask() {
    }

}

