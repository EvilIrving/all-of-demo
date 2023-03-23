package com.ygkj.warning;

import com.ygkj.warning.model.SysSendMessageHistory;
import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.warning.vo.request.SysSendMessageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(tags = "统一消息服务", value = "统一消息服务")
public interface SysSendMessageControllerApi {

    @ApiOperation("我发送的")
    CommonResult messageSendByMySelf(SysSendMessageQueryVo queryVo);

    @ApiOperation("我处理的")
    CommonResult messageHandledByMySelf(SysSendMessageQueryVo queryVo);

    @ApiOperation("发送消息")
    CommonResult sendMessage(List<SysSendMessageLog> list);

    @ApiOperation("后续处理消息")
    CommonResult handleMessage(SysSendMessageHistory history);

    @ApiOperation("单条消息详情")
    CommonResult load(String id);

    @ApiOperation("我发送的/我接收的")
    CommonResult mySendAndReceiveStationNews();

    @ApiOperation("预警类型")
    CommonResult typeEnums();
}
