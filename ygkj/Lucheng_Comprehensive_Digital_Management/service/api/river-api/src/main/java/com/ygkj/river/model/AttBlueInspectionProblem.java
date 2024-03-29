package com.ygkj.river.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lxl
 * @create 2021-08-05 21:15
 * @description 巡查问题表
 */
@Data
@ApiModel("蓝线巡查问题表")
public class AttBlueInspectionProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 本次巡查异常条目
     */
    @ApiModelProperty("本次巡查异常条目问题名称")
    private String entryName;

    @ApiModelProperty("问题描述")
    private String problemDescrip;

    /**
     * 对应巡查表编码
     */
    @ApiModelProperty("对应巡查表编码id")
    private String inspectionId;

    /**
     * 状态（1正常、2异常）--异常的进入整改 需要进行复核
     */
    @ApiModelProperty("状态（1正常、2异常）--异常的进入整改 需要进行复核")
    private Integer status;

    /**
     * 是否整改（0-无需整改 1-未整改 2-已整改）
     */
    @ApiModelProperty("是否整改（0-无需整改 1-未整改 2-已整改）默认为0")
    private Integer rectify;

    /**
     * 相关照片文件
     */
    @ApiModelProperty("相关照片以及文件 url")
    private String fileIds;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer delFlag;

    public AttBlueInspectionProblem() {
    }

}
