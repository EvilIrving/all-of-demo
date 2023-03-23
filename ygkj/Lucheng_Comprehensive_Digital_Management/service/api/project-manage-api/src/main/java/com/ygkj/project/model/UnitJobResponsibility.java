package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("单位岗位职责表")
public class UnitJobResponsibility implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("单位类型 1-运行单位 2-物业单位")
    private String unitType;


    @ApiModelProperty("岗位类型 1-岗位负责人 2-技术负责人 3-财务负责人 4-巡查负责人 5-项目负责人")
    private String jobType;


    @ApiModelProperty("岗位职责")
    private String jobResponsibilities;


    @ApiModelProperty("创建者id")
    private String creatorId;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("更新者id")
    private String updaterId;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty("更新时间")
    private Date updateTime;


    @ApiModelProperty("del_flag")
    private String delFlag;

    @ApiModelProperty("dept_id")
    private String deptId;

}
