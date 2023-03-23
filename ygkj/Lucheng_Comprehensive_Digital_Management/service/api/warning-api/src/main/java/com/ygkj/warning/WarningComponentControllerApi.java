package com.ygkj.warning;

import com.ygkj.warning.vo.request.SysSendMessageQueryVo;
import com.ygkj.warning.vo.request.WarningComponentQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import javax.validation.constraints.NotBlank;

@Api(tags = "预警组件", value = "预警组件")
public interface WarningComponentControllerApi {

    @ApiOperation("根据类型获取预警消息统计信息")
    @ApiImplicitParam(name = "type", value = "消息类型：提醒/预警", dataType = "string", paramType = "query", required = true)
    CommonResult statistic(@NotBlank(message = "请指定要统计的消息类型！") String type);

    @ApiOperation("通知预警图表")
    CommonResult chartStatistic(WarningComponentQueryVo queryVo);

    @ApiOperation("预警详表")
    CommonResult warnings(SysSendMessageQueryVo queryVo);
}
