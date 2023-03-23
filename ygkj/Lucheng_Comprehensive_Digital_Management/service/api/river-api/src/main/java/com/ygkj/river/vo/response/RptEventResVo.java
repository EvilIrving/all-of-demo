package com.ygkj.river.vo.response;

import com.ygkj.river.model.RiverInfo;
import com.ygkj.river.model.RiverPatrolTask;
import com.ygkj.river.model.RiverPatrolTaskEvent;
import com.ygkj.river.model.RiverPatrolTaskEventProb;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@Data
@ApiModel("任务巡河-事件回参")
public class RptEventResVo extends RiverPatrolTaskEvent {
    /**
     * 问题数量
     */
    @ApiModelProperty("问题数量")
    int problemNum = 0;
    /**
     * 任务信息
     */
    @ApiModelProperty("任务信息")
    RptTaskResVo taskInfo;
    /**
     * 摄像头编码列表
     */
    @ApiModelProperty("摄像头编码列表")
    List<String> cameraCodeList;

    /**
     * 问题列表
     */
    @ApiModelProperty("问题列表")
    List<RiverPatrolTaskEventProb> probResList;
}
