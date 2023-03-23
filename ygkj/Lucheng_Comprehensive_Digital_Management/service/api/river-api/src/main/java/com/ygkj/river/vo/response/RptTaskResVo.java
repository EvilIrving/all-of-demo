package com.ygkj.river.vo.response;

import com.ygkj.river.model.RiverInfo;
import com.ygkj.river.model.RiverPatrolTask;
import com.ygkj.river.model.RiverPatrolTaskEvent;
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
@ApiModel("任务巡河-回参")
public class RptTaskResVo extends RiverPatrolTask {
    /**
     * 视频数量
     */
    @ApiModelProperty("视频数量")
    int cameraNum = 0;
    /**
     * 事件列表
     */
    @ApiModelProperty("事件列表")
    List<RptEventResVo> eventList;
    /**
     * 河道信息
     */
    @ApiModelProperty("河道信息")
    RiverInfoResVo riverInfo;


}
