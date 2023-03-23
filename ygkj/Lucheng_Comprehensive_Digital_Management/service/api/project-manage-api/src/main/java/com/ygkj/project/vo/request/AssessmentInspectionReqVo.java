package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class AssessmentInspectionReqVo extends PageEntity {
    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("年度")
    private String year;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("检查时间")
    private Date inspectionTime;


    @ApiModelProperty("工程编码")
    private String supervisionProject;


    @ApiModelProperty("具体问题")
    private String problems;


    @ApiModelProperty("具体问题照片")
    private String problemsPicture;


    @ApiModelProperty("督办状态 1- 未督促 2-已督促")
    private String supervisionStatus;


    @ApiModelProperty("责任单位")
    private String responsibleUnit;


    @ApiModelProperty("责任人")
    private String responsiblePeople;


    @ApiModelProperty("处理说明")
    private String handlingInstruction;


    @ApiModelProperty("处理状态 1- 未处理 2-已处理")
    private String handlingStatus;


    @ApiModelProperty("处理图片")
    private String handlingPicture;


    @ApiModelProperty("归档状态 1- 未归档 2-已归档")
    private String archivingStatus;

    @ApiModelProperty("评价状态 1- 未评价 2-已评价")
    private String evaluationStatus;

    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;

    @ApiModelProperty("当前用户id")
    private String currentUserId;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("app督察各个页面的类型 1督察检查 2督察督办 3进度查询 4归档成功")
    private String type;
}
