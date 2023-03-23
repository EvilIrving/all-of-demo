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
@ApiModel(description = "任务巡河-问题新增/修改入参")
public class RptProbEditReqVo {
    /**
     * 问题id
     */
    @ApiModelProperty("问题id")
    private String id;

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
     * 摄像头代码
     */
    @ApiModelProperty("摄像头代码")
    private String cameraCode;

}
