package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("合同管理约定事项")
public class ConCovenant implements Serializable {

    private static final long serialVersionUID = 1L;

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


    @ApiModelProperty("服务开始时间")
    private Date serviceStartTime;

    @ApiModelProperty("服务结束时间")
    private Date serviceEndTime;

    @ApiModelProperty("约定事项文件")
    private String file;


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
