package com.ygkj.river;

import com.ygkj.river.model.HikvisionCameraRecord;
import com.ygkj.river.model.RiverPatrolTask;
import com.ygkj.river.model.RiverPatrolTaskEvent;
import com.ygkj.river.model.RiverPatrolTaskEventProb;
import com.ygkj.river.vo.request.*;
import com.ygkj.river.vo.response.RptEventResVo;
import com.ygkj.river.vo.response.RptProbResVo;
import com.ygkj.river.vo.response.RptTaskResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@Api(tags = "任务巡河")
public interface RiverPatrolTaskControllerApi {

    @ApiOperation(value = "查询与河流关联的摄像头", response = HikvisionCameraRecord.class)
    CommonResult camerasOfRiver(String riverCode);

    @ApiOperation(value = "根据摄像头编码按顺序查询摄像头数据", response = HikvisionCameraRecord.class)
    CommonResult cameraByCode(String cameraCode);

    /**
     * -------------------------任务巡河-任务---------------------------------
     */
    @ApiOperation(value = "分页查询所有的任务", response = RptTaskResVo.class)
    CommonResult taskPage(RptTaskQueryReqVo reqVo);

    @ApiOperation("增加/更新任务;当id为空的时候增加id不空时更新")
    CommonResult addOrUpdateTask(RptTaskEditReqVo reqVo);

    @ApiOperation("删除一项任务")
    CommonResult delTask(String id);

    @ApiOperation(value = "查询一个任务", response = RptTaskResVo.class)
    CommonResult getOneTask(String id);

    /**
     * ------------------------任务巡河-事件-----------------------------------
     */

    @ApiOperation(value = "分页查询所有的事件", response = RptEventResVo.class)
    CommonResult eventPage(RptEventQueryReqVo reqVo);

    @ApiOperation("增加/更新事件;当id为空的时候增加id不空时更新")
    CommonResult addOrUpdateEvent(RptEventEditReqVo reqVo);

    @ApiOperation("删除一项事件")
    CommonResult delEvent(String id);

    @ApiOperation(value = "查询一个事件", response = RptEventResVo.class)
    CommonResult getOneEvent(String id);

    @ApiOperation("任务巡河事件列表导出")
    void exportTaskEvent(RptEventQueryReqVo vo, HttpServletResponse response);

    /**
     * ------------------------任务巡河-问题-----------------------------------
     */
    @ApiOperation("增加/更新问题;当id为空的时候增加id不空时更新")
    CommonResult addOrUpdateProblem(RptProbEditReqVo reqVo);

    @ApiOperation("删除一项问题")
    CommonResult delProblem(String id);

    @ApiOperation(value = "查询摄像头查询问题", response = RptProbResVo.class)
    CommonResult getOneProblemByCamera(@ApiParam("事件id") String eventId, @ApiParam("摄像头编码") String cameraCode);

    @ApiOperation(value = "根据id查询一个问题", response = RptProbResVo.class)
    CommonResult getOneProblemById(@ApiParam("问题id") String probId);


}
