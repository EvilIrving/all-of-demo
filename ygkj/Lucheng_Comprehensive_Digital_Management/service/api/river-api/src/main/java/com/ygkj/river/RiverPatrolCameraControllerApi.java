package com.ygkj.river;

import com.ygkj.river.model.RiverPatrolCameraEvent;
import com.ygkj.river.model.RiverPatrolTask;
import com.ygkj.river.vo.request.*;
import com.ygkj.river.vo.response.RpcEventResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@Api(tags = "视频巡河")
public interface RiverPatrolCameraControllerApi {
    /**
     * -------------------------视频巡河---------------------------------
     */
    @ApiOperation(value = "查询所有的视频巡河事件", response = RpcEventResVo.class)
    CommonResult cameraEventPage(RpcEventQueryReqVo reqVo);

    @ApiOperation("增加/更新视频巡河事件;当id为空的时候增加id不空时更新")
    CommonResult addOrUpdateCameraEvent(RpcEventEditReqVo reqVo);

    @ApiOperation("删除一项视频巡河事件")
    CommonResult delCameraEvent(String id);

    @ApiOperation(value = "查询一个视频巡河事件", response = RpcEventResVo.class)
    CommonResult getOneCameraEvent(String id);

    @ApiOperation(value = "视频巡河事件列表导出")
    void exportCameraEvent(RpcEventQueryReqVo vo, HttpServletResponse response);

}
