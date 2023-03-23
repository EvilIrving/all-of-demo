package com.ygkj.project;

import com.ygkj.project.model.MessageReminder;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "消息提醒")
public interface MessageReminderControllerApi {
    @ApiOperation("消息列表")
    CommonResult loadMessage(MessageReminder reminder);

    @ApiOperation("消息统计")
    CommonResult countMessage(MessageReminder reminder);

    @ApiOperation("已读消息")
    CommonResult readMessage(Integer id);

    @ApiOperation("全部已读")
    CommonResult readAllMessage(MessageReminder reminder);
}
