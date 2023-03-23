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
@ApiModel(description = "任务巡河-任务新增/更新入参")
public class RptTaskEditReqVo {
    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private String id;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String taskName;

    /**
     * 河流代码
     */
    @ApiModelProperty("河流代码")
    private String riverCode;

    /**
     * 河长名称
     */
    @ApiModelProperty("河长名称")
    private String riverManageName;

    /**
     * 河长联系方式
     */
    @ApiModelProperty("河长联系方式")
    private String riverManagePhone;

    /**
     * 摄像头代码，多个摄像头用逗号隔开
     */
    @ApiModelProperty("摄像头代码，多个摄像头用逗号隔开，和摄像头名称顺序对应")
    private String cameraCode;

    /**
     * 摄像头名称，多个摄像头名称用逗号隔开
     */
    @ApiModelProperty("摄像头名称，多个摄像头名称用逗号隔开，和摄像头编码顺序对应")
    private String cameraName;

    /**
     * 巡河频次：1小时1次；2小时1次；3小时1次；6小时1次；12小时1次；（数据字典）
     */
    @ApiModelProperty("巡河频次：1小时1次；2小时1次；3小时1次；6小时1次；12小时1次；（数据字典）")
    private String patrolFreeq;

//    /**
//     * 行政区划代码
//     */
//    @ApiModelProperty("行政区划代码")
//    private String adcd;
}
