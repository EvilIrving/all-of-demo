package com.ygkj.project;

import com.ygkj.project.vo.request.MtDmDailyTaskReqVo;
import com.ygkj.project.vo.request.MtTaskNodeReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2022-04-21 17:07
 * @description
 */
@Api(tags = "隐患处理接口")
public interface HiddenDangerControllerApi {

//    @ApiOperation("获取隐患列表（日常任务）")
//    CommonResult getHiddenDangerList(MtDmDailyTaskReqVo reqVo);

    @ApiOperation("获取隐患详情信息")
    CommonResult getHiddenDangerDetail(String id);

    @ApiOperation("处理人提交处理信息")
    CommonResult implementSubmit(MtTaskNodeReqVo reqVo);

    @ApiOperation("审核人提交审核信息")
    CommonResult submitProgressInfo(MtTaskNodeReqVo reqVo);

    @ApiOperation("发送提醒消息")
    CommonResult sendNoticeMsg(String id);

    @ApiOperation("消息提醒列表")
    CommonResult msgList();

    @ApiOperation("消息已读")
    CommonResult msgRead(String id);

    @ApiOperation("app隐患数量统计")
    CommonResult hiddenAppTotal();

}
