package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel("汛前检查")
public class BhLgMpFc {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 汛前检查报告
     */
    @ApiModelProperty("汛前检查报告文件ids ")
    private String aid;

    /**
     * 工程编号
     */
    @ApiModelProperty("工程编码")
    private String pid;

    /**
     * 检查时间
     */
    @ApiModelProperty("检查时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tm;

    /**
     * 汛后检查报告
     */
    @ApiModelProperty("汛后检查报告")
    private String afFloodRpt;

    /**
     * 汛后检查完成时间
     */
    @ApiModelProperty("汛后检查完成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date afFloodTm;

    /**
     * 白蚁检查报告
     */
    @ApiModelProperty("白蚁检查报告")
    private String termiteCheckRpt;

    /**
     * 白蚁检查完成时间
     */
    @ApiModelProperty("白蚁检查完成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date termiteCheckTm;

    /**
     * 带队检查人
     */
    @ApiModelProperty("带队检查人")
    private String checkUid;

    /**
     * 参加人员
     */
    @ApiModelProperty("参加人员")
    private String joinUid;

    @ApiModelProperty("参加人员名称")
    private String joinUser;

    /**
     * 隐患数量
     */
    @ApiModelProperty("隐患数量")
    private Integer hidCount;

    /**
     * 检查照片
     */
    @ApiModelProperty("检查照片")
    private String checkPic;
    /**
     * 数据来源  1-标化 2-本地新增
     */
    @ApiModelProperty("数据来源")
    private Integer sourceType;

    /**
     * 问题类型  一般问题、较重问题、严重问题
     */
    @ApiModelProperty("问题类型")
    private String problemType;

    @ApiModelProperty("年度检查工程运行管理检查记录表")
    private String runActionRecord;

    @ApiModelProperty("汛前及年度检查工程外观检查记录表")
    private String projCheckRecord;


}
