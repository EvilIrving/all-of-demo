package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("运行单位单位职责")
public class UnitResponsibility implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("机构全称")
    private String unitName;


    @ApiModelProperty("单位地址")
    private String unitAddress;


    @ApiModelProperty("单位性质")
    private String unitNature;


    @ApiModelProperty("主管部门")
    private String mainDepartment;


    @ApiModelProperty("单位联系电话")
    private String unitPhone;


    @ApiModelProperty("单位汛期值班电话")
    private String unitPhoneInFloodReason;


    @ApiModelProperty("单位传真")
    private String unitFax;


    @ApiModelProperty("单位职责")
    private String unitResponsibilities;

    @ApiModelProperty("单位类型 1-运行单位 2-物业单位")
    private String unitType;

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
