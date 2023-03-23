package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ProjectOversee {

    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty("工程编码")
    private String projectCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "督办时间(不用传)")
    private Date createTime;

    @ApiModelProperty(value = "督办人(不用传)")
    private String createId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("截止时间 yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    @ApiModelProperty("问题描述")
    private String problemDescribe;

    @ApiModelProperty("问题附件,多个逗号分隔")
    private String problemFile;

    @ApiModelProperty("填报人手机号")
    private String reportPhone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "整改时间(不用传)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date workTime;

    @ApiModelProperty("整改说明(修改传)")
    private String workDescribe;

    @ApiModelProperty("整改附件,多个逗号分隔(修改传)")
    private String workFile;

    @ApiModelProperty("验证码")
    @TableField(exist = false)
    private String verificationCode;
}
