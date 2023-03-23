package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 视频巡河表
 * @date 2021-04-07
 */
@Data
@ApiModel("视频巡河-事件")
public class RiverPatrolCameraEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件id
     */
    @ApiModelProperty("事件id")
    private String id;

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
     * 河道流域
     */
    @ApiModelProperty("河道流域")
    private String riverBas;

    /**
     * 抓拍方式:自动、手动
     */
    @ApiModelProperty("抓拍方式:自动、手动")
    private String captureMode;

    /**
     * 抓拍时间/事件时间
     */
    @ApiModelProperty("抓拍时间/事件时间")
    private Date eventTime;

    /**
     * 事件类型
     */
    @ApiModelProperty("事件类型")
    private String eventType;

    /**
     * 事件描述
     */
    @ApiModelProperty("事件描述")
    private String eventDesc;

    /**
     * 图片链接
     */
    @ApiModelProperty("图片链接")
    private String imageLink;

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

    public RiverPatrolCameraEvent() {
    }

}
