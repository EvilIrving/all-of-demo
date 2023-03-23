package com.ygkj.project.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 非标化表,鹿城新增  专项检查报告
 * @author
 * @date 2022-04-28
 */
@Data
@ApiModel("非标化表,鹿城新增  专项检查报告")
public class BhLgMpSp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 检查时间
     */
    @ApiModelProperty("检查时间")
    private Date tm;

    /**
     * 检查人员
     */
    @ApiModelProperty("检查人员")
    private String checkUid;

    /**
     * 检查人员
     */
    @ApiModelProperty("检查人员名称")
    private String checkUser;

    /**
     * 组织单位
     */
    @ApiModelProperty("组织单位")
    private String unitName;

    /**
     * 组织单位id
     */
    @ApiModelProperty("组织单位id")
    private String unitId;

    /**
     * 检查照片
     */
    @ApiModelProperty("检查照片")
    private String checkPic;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<SysFile> checkPicList;


    /**
     * 存在问题
     */
    @ApiModelProperty("存在问题")
    private String problemDesc;

    /**
     * 问题类型
     */
    @ApiModelProperty("问题类型")
    private String problemType;

    /**
     * 检查报告
     */
    @ApiModelProperty("检查报告")
    private String fileIds;

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<SysFile> fileList;

    @ApiModelProperty("工程id")
    private String pid;


    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private Integer delFlag;

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

    public BhLgMpSp() {
    }

}

