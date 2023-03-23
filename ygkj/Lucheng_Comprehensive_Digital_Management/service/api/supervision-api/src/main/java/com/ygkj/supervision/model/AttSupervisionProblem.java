package com.ygkj.supervision.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @description att_supervision_problem
 * @date 2021-05-08
 */
@Data
@ApiModel("att_supervision_problem")
@TableName("att_supervision_problem")
public class AttSupervisionProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题编码
     */
    @ApiModelProperty("问题编码")
    private String problemCode;

    /**
     * 检查事项编码
     */
    @ApiModelProperty("问题所属检查事项编码")
    private String matterCode;

    /**
     * 项目编码
     */
    @ApiModelProperty("问题所属项目编码")
    private String projectCode;

    /**
     * 问题名称/描述
     */
    @ApiModelProperty("问题名称/描述")
    private String problemName;

    /**
     * 问题严重程度/分类
     */
    @ApiModelProperty("问题严重程度/分类  YB-一般   YZ-严重   JZ-较重")
    private String problemType;

    /**
     * 问题状态 ( "确认审核-QR    未通过-WG   通过-TG   整改未审核-ZG    未整改-WZ    整改上报到未审核-SW ")
     * 审核通过-QR    审核未通过-WG   整改后未审核-ZG    下达后未查看-WZ
     */
    @ApiModelProperty("问题状态")
    private String problemStatus;

    /**
     * 问题所属表单 描述编码
     */
    @ApiModelProperty("问题描述")
    private String problemProduce;

    @ApiModelProperty("问题图片1")
    private String problemPic1;

    @ApiModelProperty("问题图片2")
    private String problemPic2;

    @ApiModelProperty("问题图片3")
    private String problemPic3;

    @ApiModelProperty("问题图片4")
    private String problemPic4;

    @ApiModelProperty("问题图片5")
    private String problemPic5;

    @ApiModelProperty("问题图片6")
    private String problemPic6;

    @ApiModelProperty("问题附件")
    private String problemFile;

    /**
     * 整改意见
     */
    @ApiModelProperty("整改意见")
    private String rectifyOpinion;


    @ApiModelProperty("整改反馈")
    private String rectifyFeedback;

    /**
     * 整改图片1
     */
    @ApiModelProperty("整改图片1")
    private String rectifyPic1;

    /**
     * 整改图片2
     */
    @ApiModelProperty("整改图片2")
    private String rectifyPic2;

    /**
     * 整改图片3
     */
    @ApiModelProperty("整改图片3")
    private String rectifyPic3;


    /**
     * 整改图片4
     */
    @ApiModelProperty("整改图片4")
    private String rectifyPic4;

    /**
     * 整改图片5
     */
    @ApiModelProperty("整改图片5")
    private String rectifyPic5;


    /**
     * 整改图片6
     */
    @ApiModelProperty("整改图片6")
    private String rectifyPic6;


    /**
     * 整改附件
     */
    @ApiModelProperty("整改附件")
    private String rectifyFile;

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

    public AttSupervisionProblem() {
    }

}

