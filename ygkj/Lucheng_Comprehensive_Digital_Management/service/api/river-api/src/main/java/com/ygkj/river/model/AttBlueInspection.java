package com.ygkj.river.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @create 2021-08-05 21:12
 * @description 蓝线巡查表
 */
@Data
@ApiModel("蓝线巡查表")
public class AttBlueInspection implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 下发整改名称
     */
    @ApiModelProperty("下发整改名称")
    private String rectifyName;

    /**
     * 派发人
     */
    @ApiModelProperty("派发人名称")
    private String distributor;

    @ApiModelProperty("派发人联系电话")
    private String distributorPhone;

    /**
     * 指定首次巡查人员
     */
    @ApiModelProperty("指定首次巡查人员")
    private String inspector;

    /**
     * 首次巡查人员id
     */
    @ApiModelProperty("指定首次巡查人员id")
    private String inspectorId;

    /**
     * 巡查阶段 1-开工 2-施工 3-完工
     */
    @ApiModelProperty("巡查阶段 1-开工 2-施工 3-完工")
    private Integer stage;

    /**
     * 巡检项目名
     */
    @ApiModelProperty("关联巡检项目名")
    private String projectName;

    /**
     * 巡检项目id
     */
    @ApiModelProperty("关联巡检项目id")
    private String projectId;

    /**
     * 巡查开始时间
     */
    @ApiModelProperty("巡检开始时间")
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private Date patrolBeginTime;

    /**
     * 巡查结束时间
     */
    @ApiModelProperty("巡查结束时间")
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private Date patrolEndTime;

    @ApiModelProperty("首次巡查结果  1-异常  0-正常")
    private Integer patrolResult;

    /**
     * 整改开始时间
     */
    @ApiModelProperty("整改开始时间")
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rectifyBeginTime;

    /**
     * 整改结束时间
     */
    @ApiModelProperty("整改结束时间")
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rectifyEndTime;

    /**
     * 复核巡检人员
     */
    @ApiModelProperty("复核巡检人员")
    private String reviewer;

    /**
     * 复核巡检人员id
     */
    @ApiModelProperty("复核巡检人员id")
    private String reviewerId;

    @ApiModelProperty("复核结束时间")
    private Date reviewerEndTime;

    @ApiModelProperty("复核巡查结果 1-不正常  0-正常")
    private Integer reviewResult;

    /**
     * 鉴定结果
     */
    @ApiModelProperty("鉴定结果")
    private String appraisalResult;

    /**
     * 距离整改截止日期前 x 天通知
     */
    @ApiModelProperty("距离整改截止日期前 x 天通知，为null 为不进行通知")
    private Integer rectifyNoticeTime;

    /**
     * 整改通知人员
     */
    @ApiModelProperty("整改通知人员")
    private String rectifyPeople;

    /**
     * 确认是否具备隐患 1-是 0-否
     */
    @ApiModelProperty("确认是否具备隐患 1-是 0-否")
    private Integer hiddenDanger;

    /**
     * 结办人员
     */
    @ApiModelProperty("结办人员")
    private String coordinator;

    /**
     * 结办人员id
     */
    @ApiModelProperty("结办人员id")
    private String coordinatorId;

    @ApiModelProperty("结办说明")
    private String coordinatorProduce;

    @ApiModelProperty("结办文件")
    private String coordinatorFile;

    @ApiModelProperty("结办文件ids")
    private String coordinatorFileIds;

    @ApiModelProperty("结办时间")
    private Date coordinatorTime;

    @ApiModelProperty("结办后是否通知 1-是 0-否")
    private Integer coordinatorNotify;

    @ApiModelProperty("持续时间（天）")
    private Integer pendingTime;

    @ApiModelProperty("本次巡检是否已完成 1-是 0-否")
    private Integer resultStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer delFlag;

    public AttBlueInspection() {
    }

}
