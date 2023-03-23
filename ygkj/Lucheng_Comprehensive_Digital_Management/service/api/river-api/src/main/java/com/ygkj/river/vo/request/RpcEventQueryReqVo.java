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
@ApiModel("视频巡河-事件查询条件")
public class RpcEventQueryReqVo {
    /**
     * 流域：飞云江；鳌江；瓯江；椒江；其他；
     */
    @ApiModelProperty("流域：飞云江；鳌江；瓯江；椒江；其他；")
    String riverBas;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    String adcd;
    /**
     * 事件类型：位移；异常行为；蓝藻；漂浮物；钓鱼
     */
    @ApiModelProperty("事件类型：位移；异常行为；蓝藻；漂浮物；钓鱼")
    String eventType;
    /**
     * 事件时间上限
     */
    @ApiModelProperty("事件时间上限")
    String eventSTime;
    /**
     * 事件时间下限
     */
    @ApiModelProperty("事件时间下限")
    String eventETime;
    /**
     * 抓拍方式：自动；手动
     */
    @ApiModelProperty("抓拍方式：自动；手动")
    String captureMode;

    /**
     * 摄像头名称
     */
    @ApiModelProperty("摄像头名称")
    String cameraName;

    /**
     * 页码，最小为1
     */
    @ApiModelProperty("页码，最小为1,仅分页接口有效")
    int pageNum = 1;
    /**
     * 页面大小，最小为1
     */
    @ApiModelProperty("页面大小，最小为1,仅分页接口有效")
    int pageSize = 10;
}
