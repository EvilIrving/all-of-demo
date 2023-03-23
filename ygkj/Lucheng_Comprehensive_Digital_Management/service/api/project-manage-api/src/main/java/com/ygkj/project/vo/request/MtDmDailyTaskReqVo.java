package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-18 18:01
 * @description
 */
@Data
public class MtDmDailyTaskReqVo extends PageEntity {

    @ApiModelProperty("年份筛选")
    private String year;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("工程id")
    private String prcd;

    @ApiModelProperty("隐患状态（1-已处理、2-待处理、3-处理中）")
    private Integer hiddenDangerStatus;

    @ApiModelProperty("日期筛选 yyyy-MM-dd 格式")
    private String date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    /**
     * 指导意见
     */
    @ApiModelProperty("指导意见")
    private String instructions;

    @ApiModelProperty("创建人id")
    private String createId;

}
