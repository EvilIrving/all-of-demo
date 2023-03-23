package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description 非标化表,鹿城新增。 汛后检查表
 * @author
 * @date 2022-04-28
 */
@Data
@ApiModel("非标化表,鹿城新增。 汛后检查表")
public class BhLgMpAf implements Serializable {

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tm;

    /**
     * 参加人员
     */
    @ApiModelProperty("参加人员")
    private String joinUid;

    /**
     * 参加人员
     */
    @ApiModelProperty("参加人员名称")
    private String joinUser;

    /**
     * 水管单位负责人
     */
    @ApiModelProperty("水管单位负责人")
    private String responseUid;

    /**
     * 水管单位负责人
     */
    @ApiModelProperty("水管单位负责人名称")
    private String responseUser;

    /**
     * 隐患数量
     */
    @ApiModelProperty("隐患数量")
    private Integer hitCount;

    /**
     * 问题类型
     */
    @ApiModelProperty("问题类型")
    private String problemType;

    /**
     * 检查照片
     */
    @ApiModelProperty("检查照片")
    private String checkPic;

    @TableField(exist = false)
    private List<SysFile> checkPicList;

    @TableField(exist = false)
    private List<SysFile> fileList;

    /**
     * 定期（汛后）检查记录表
     */
    @ApiModelProperty("定期（汛后）检查记录表")
    private String fileIds;

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

    public BhLgMpAf() {
    }

}

