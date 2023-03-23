package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BhLgLmrDisDomReqVo extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调度令id
     */
    @ApiModelProperty("调度令id")
    private String id;

    /**
     * 工程编号
     */
    @ApiModelProperty("工程编号")
    private String pid;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private String dYear;

    /**
     * 调令下达时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("调令下达时间")
    private Date tm;

    /**
     * 调令编号
     */
    @ApiModelProperty("调令编号")
    private String sOrderCode;

    /**
     * 调令闸门开启时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("调令闸门开启时间")
    private Date gateOpenTm;

    /**
     * 调令闸门关闭时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("调令闸门关闭时间")
    private Date gateCloseTm;

    /**
     * 数据来源 1-标化 2-本地新增
     */
    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private Integer sourceType;

    /**
     * 调度类型 1-上级调度 2-计划内调度
     */
    @ApiModelProperty("调度类型 1-上级调度 2-计划内调度")
    private String domType;

    @ApiModelProperty("文件上传人id")
    private String uploader;


    @ApiModelProperty("冗余字段 文件上传人名称")
    private String uploaderName;


    @ApiModelProperty("本地新增上传文件ids")
    private String uploadfile;
}
