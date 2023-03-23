package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("单位岗位设置")
public class UnitPositionSet implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("工程编码")
    private String projectCode;


    @ApiModelProperty("工程名称")
    private String projectName;


    @ApiModelProperty("工程类别")
    private String projectCategory;

    @ApiModelProperty("工程规模")
    private String projectScale;


    @ApiModelProperty("所属乡镇（街道）")
    private String townAdnm;


    @ApiModelProperty("负责人json数组")
    private String jsonarray;


    @ApiModelProperty("单位类型 1-运行单位 2-物业单位")
    private String type;

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

    @TableField(exist = false)
    @ApiModelProperty("负责人数组")
    private List<UnitPositionSetBean> array;

    @TableField(exist = false)
    @ApiModelProperty("单位负责人")
    private String unitLeader;

    @TableField(exist = false)
    @ApiModelProperty("单位负责人电话")
    private String unitLeaderPhone;

    @TableField(exist = false)
    @ApiModelProperty("技术负责人")
    private String technicalLeader;

    @TableField(exist = false)
    @ApiModelProperty("技术负责人电话")
    private String technicalLeaderPhone;

    @TableField(exist = false)
    @ApiModelProperty("财务负责人")
    private String financialLeader;

    @TableField(exist = false)
    @ApiModelProperty("财务负责人电话")
    private String financialLeaderPhone;

    @TableField(exist = false)
    @ApiModelProperty("巡查负责人")
    private String patrolLeader;

    @TableField(exist = false)
    @ApiModelProperty("巡查负责人电话")
    private String patrolLeaderPhone;

    @TableField(exist = false)
    @ApiModelProperty("安全负责人")
    private String safetyLeader;

    @TableField(exist = false)
    @ApiModelProperty("安全负责人电话")
    private String safetyLeaderPhone;

}
