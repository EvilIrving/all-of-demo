package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ConCovenantReqVo extends PageEntity {
    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("业主单位id")
    private String proprietorUnitId;


    @ApiModelProperty("业主单位名称")
    private String proprietorUnitName;


    @ApiModelProperty("物业单位id")
    private String propertyUnitId;


    @ApiModelProperty("物业单位名称")
    private String propertyUnitName;

    @ApiModelProperty("工程编码")
    private String prcd;

    @ApiModelProperty("工程名称")
    private String projectName;


    @ApiModelProperty("服务地点")
    private String serviceLocation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("服务开始时间")
    private Date serviceStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("服务结束时间")
    private Date serviceEndTime;


    @ApiModelProperty("约定事项文件")
    private String file;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;


    @ApiModelProperty("时间筛选-开始时间")
    private String startTime;

    @ApiModelProperty("时间筛选-结束时间")
    private String endTime;
}
