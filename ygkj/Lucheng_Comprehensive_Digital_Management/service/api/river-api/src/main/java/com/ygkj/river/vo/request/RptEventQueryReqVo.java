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
@ApiModel("任务巡河-事件查询入参")
public class RptEventQueryReqVo {

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    String taskId;
    /**
     * 流域名称
     */
    @ApiModelProperty("流域名称")
    String riverBas;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    String adcd;

    /**
     * 巡河起始时间
     */
    @ApiModelProperty("巡河起始时间")
    String patrolSTime;
    /**
     * 巡河结束时间
     */
    @ApiModelProperty("巡河结束时间")
    String patrolETime;
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
