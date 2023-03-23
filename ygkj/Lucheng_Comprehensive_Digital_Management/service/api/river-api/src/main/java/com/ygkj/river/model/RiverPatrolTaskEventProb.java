package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 任务巡河事件问题表
 * @date 2021-04-06
 */
@Data
@ApiModel("任务巡河-问题")
public class RiverPatrolTaskEventProb implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题id
     */
    @ApiModelProperty("问题id")
    private String id;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private String taskId;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String taskName;

    /**
     * 巡河时间
     */
    @ApiModelProperty("巡河时间")
    private Date patrolTime;

    /**
     * 事件id
     */
    @ApiModelProperty("事件id")
    private String eventId;

    /**
     * 事件类型
     */
    @ApiModelProperty("事件类型")
    private String eventType;

    /**
     * 图片链接
     */
    @ApiModelProperty("图片链接")
    private String imageLink;

    /**
     * 河流代码
     */
    @ApiModelProperty("河流代码")
    private String riverCode;

    /**
     * 河流名称
     */
    @ApiModelProperty("河流名称")
    private String riverName;

    /**
     * 摄像头代码
     */
    @ApiModelProperty("摄像头代码")
    private String cameraCode;

    /**
     * 摄像头名称
     */
    @ApiModelProperty("摄像头名称")
    private String cameraName;

    /**
     * 行政区划代码
     */
    @ApiModelProperty("行政区划代码")
    private String adcd;

    /**
     * 行政区划名称
     */
    @ApiModelProperty("行政区划名称")
    private String adnm;

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
    private int delFlag;

    /**
     * 部门id
     */
    private String deptId;

    public RiverPatrolTaskEventProb() {
    }

}
