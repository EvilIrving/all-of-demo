package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class BhLgMpAc {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("年度检查报告")
    private String aid;

    @ApiModelProperty("工程编号")
    private String pid;

    @ApiModelProperty("检查时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tm;

    @ApiModelProperty("带队检查人")
    private String checkUid;

    @ApiModelProperty("参加人员")
    private String joinUid;

    @ApiModelProperty("隐患数量")
    private Integer hidCount;

    @ApiModelProperty("检查照片")
    private String checkPic;

    /**
     * 数据来源  1-标化 2-本地新增
     */
    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private Integer sourceType;

    /**
     * 问题类型  一般问题、较重问题、严重问题
     */
    @ApiModelProperty("问题类型 一般问题、较重问题、严重问题")
    private String problemType;

    @ApiModelProperty("年度检查工程运行管理检查记录表")
    private String runActionRecord;

    @ApiModelProperty("汛前及年度检查工程外观检查记录表")
    private String projCheckRecord;

    @TableField(exist = false)
    private List<SysFile> aidFileList;
    @TableField(exist = false)
    private List<SysFile> checkPicList;
    @TableField(exist = false)
    private List<SysFile> runActionRecordFiles;
    @TableField(exist = false)
    private List<SysFile> projCheckRecordFiles;


}
