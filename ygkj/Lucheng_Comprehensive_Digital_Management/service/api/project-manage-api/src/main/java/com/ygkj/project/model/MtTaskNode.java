package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-21 17:34
 * @description
 */
@Data
@ApiModel("维修养护任务节点 （隐患处理数据）")
public class MtTaskNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private String taskId;

    /**
     * 执行顺序
     */
    @ApiModelProperty("执行顺序")
    private Integer sort;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private Date tm;

    /**
     * 操作人员
     */
    @ApiModelProperty("操作人员")
    private String operator;

    @ApiModelProperty("操作人员id")
    private String operatorId;

    /**
     * 意见
     */
    @ApiModelProperty("意见")
    private String opinion;

    /**
     * 状态（待处理、已通过、已驳回、已审核）
     */
    @ApiModelProperty("状态（1待处理、2已通过、3已驳回、4已处理、5待审核 、6归档 ）")
    private Integer status;

    /**
     * 处理前图片
     */
    @ApiModelProperty("处理前图片")
    private String imagesBefore;

    @TableField(exist = false)
    private List<SysFile> imageBeforeList;

    /**
     * 处理后图片
     */
    @ApiModelProperty("处理后图片")
    private String imagesAfter;

    @TableField(exist = false)
    private List<SysFile> imageAfterList;

    /**
     * 处理文件
     */
    @ApiModelProperty("处理文件")
    private String fileIds;

    @ApiModelProperty("是否提醒 0-未提醒 1-已提醒")
    private Integer notice;

    @TableField(exist = false)
    private List<SysFile> fileList;

    @TableField(exist = false)
    private Object info;


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

    public MtTaskNode() {
    }

}

