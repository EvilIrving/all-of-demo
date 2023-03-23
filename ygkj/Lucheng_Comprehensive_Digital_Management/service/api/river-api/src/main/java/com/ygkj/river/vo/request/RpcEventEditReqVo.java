package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/4/7
 */
@Data
@ApiModel(description = "视频巡河-事件新增/更新入参")
public class RpcEventEditReqVo {
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
     * 河流代码
     */
    @ApiModelProperty("河流代码")
    private String riverCode;

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
}
