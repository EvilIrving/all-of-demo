package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@ApiModel("任务巡河-问题查询入参")
@Data
public class RptProbQueryReqVo {
    /**
     * 摄像头编码
     */
    @ApiModelProperty("摄像头编码")
    String cameraCode;
    /**
     * 任务巡河事件id
     */
    @ApiModelProperty("任务巡河事件id")
    String eventId;
    /**
     * 页码，最小为1
     */
    @ApiModelProperty("页码，最小为1；仅在需要分页的接口上有效")
    int pageNum = 1;
    /**
     * 页面大小，最小为1
     */
    @ApiModelProperty("页面大小，最小为1；仅在需要分页的接口上有效")
    int pageSize = 10;

}
