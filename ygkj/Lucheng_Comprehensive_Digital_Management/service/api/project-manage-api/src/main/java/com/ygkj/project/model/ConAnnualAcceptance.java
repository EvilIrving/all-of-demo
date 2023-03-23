package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("合同管理-合同验收-年度验收")
public class ConAnnualAcceptance implements Serializable {

    private static final long serialVersionUID = 1L;


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


    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("创建人")
    private String createId;


    @ApiModelProperty("修改时间")
    private Date updateTime;


    @ApiModelProperty("修改人")
    private String updateId;

    @TableField(exist = false)
    private List<SysFile> files;

}
