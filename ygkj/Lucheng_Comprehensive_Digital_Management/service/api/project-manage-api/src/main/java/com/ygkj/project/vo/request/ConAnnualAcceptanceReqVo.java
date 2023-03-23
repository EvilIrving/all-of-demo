package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ConAnnualAcceptanceReqVo extends PageEntity {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("年度")
    private String year;

    @ApiModelProperty("物业单位id")
    private String propertyUnitId;


    @ApiModelProperty("物业单位名称")
    private String propertyUnitName;


    @ApiModelProperty("运行单位id")
    private String runUnitId;


    @ApiModelProperty("运行单位名称")
    private String runUnitName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("运行单位验收时间")
    private Date runAcceptanceTime;


    @ApiModelProperty("运行单位验收意见")
    private String runAcceptanceComments;


    @ApiModelProperty("水行政主管部门id")
    private String waterAdministrationUnitId;


    @ApiModelProperty("水行政主管部门名称")
    private String waterAdministrationUnitName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("水行政主管部门验收时间")
    private Date waterAcceptanceTime;


    @ApiModelProperty("水行政主管部门验收意见")
    private String waterAcceptanceComments;

    @ApiModelProperty("验收附件")
    private String appendix;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;
}
