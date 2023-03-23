package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("非标化表,鹿城新增。 控运计划表")
public class BhControlPlan extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private String year;

    /**
     * 工程id
     */
    @ApiModelProperty("工程id")
    private String pid;

    /**
     * 控运文件
     */
    @ApiModelProperty("控运文件")
    private String fileIds;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    @TableField(exist = false)
    @ApiModelProperty("文件列表")
    private List<SysFile> fileList;
}
