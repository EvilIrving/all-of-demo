package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("合同管理考核管理督察检查")
public class AssessmentInspection implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("年度")
    private String year;


    @ApiModelProperty("检查时间")
    private Date inspectionTime;


    @ApiModelProperty("工程编码")
    private String supervisionProject;

    @ApiModelProperty("工程名称")
    @TableField(exist = false)
    private String projectName;

    @ApiModelProperty("具体问题")
    private String problems;


    @ApiModelProperty("具体问题照片")
    private String problemsPicture;

    @ApiModelProperty("具体问题照片集合")
    @TableField(exist = false)
    private List<SysFile> problemsPictureList;

    @ApiModelProperty("督办状态 1- 未督促 2-已督促")
    private String supervisionStatus;


    @ApiModelProperty("责任单位")
    private String responsibleUnit;

    @ApiModelProperty("责任单位名称")
    @TableField(exist = false)
    private String responsibleUnitName;

    @ApiModelProperty("责任人")
    private String responsiblePeople;

    @ApiModelProperty("责任人名称")
    @TableField(exist = false)
    private String responsiblePeopleName;

    @ApiModelProperty("处理说明")
    private String handlingInstruction;


    @ApiModelProperty("处理状态 1- 未处理 2-已处理")
    private String handlingStatus;


    @ApiModelProperty("处理图片")
    private String handlingPicture;

    @ApiModelProperty("处理图片集合")
    @TableField(exist = false)
    private List<SysFile> handlingPictureList;

    @ApiModelProperty("归档状态 1- 未归档 2-已归档")
    private String archivingStatus;

    @ApiModelProperty("评价状态 1- 未评价 2-已评价")
    private String evaluationStatus;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;


    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("创建人")
    private String createId;

    @ApiModelProperty("处理人名称，创建人名称")
    @TableField(exist = false)
    private String createName;

    @ApiModelProperty("修改时间")
    private Date updateTime;


    @ApiModelProperty("修改人")
    private String updateId;

}
