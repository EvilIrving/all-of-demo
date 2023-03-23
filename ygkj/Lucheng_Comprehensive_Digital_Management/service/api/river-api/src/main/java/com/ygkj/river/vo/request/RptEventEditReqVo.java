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
@ApiModel(description = "任务巡河-事件新增/修改入参")
public class RptEventEditReqVo {
    /**
     * 事件id
     */
    @ApiModelProperty("事件id")
    private String id;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private String taskId;

    /**
     * 巡河方式：自动或手动
     */
    @ApiModelProperty("巡河方式：自动或手动")
    private String patrolMode;

    /**
     * 巡河时间
     */
    @ApiModelProperty("巡河时间")
    private Date patrolTime;


}
