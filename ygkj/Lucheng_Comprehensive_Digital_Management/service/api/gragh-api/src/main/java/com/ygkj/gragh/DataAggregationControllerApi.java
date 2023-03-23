package com.ygkj.gragh;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Api(tags = "大屏组件-----数据汇聚", value = "大屏组件-----数据汇聚")
@Validated
public interface DataAggregationControllerApi {

    @ApiOperation("分类统计")
    @ApiImplicitParam(name = "type", value = "统计类型：全部/在线/离线", paramType = "query", dataType = "string")
    CommonResult ClassificationStatistics(@NotBlank(message = "请指定一种统计类型！") String type);

    @ApiOperation("在线率统计")
    @ApiImplicitParam(name = "type", value = "统计类型：雨情/水情/工情/视频", paramType = "query", dataType = "string")
    CommonResult onlineRateStatistics(@NotBlank(message = "请指定一种统计类型！") String type, String year);

    @ApiOperation("c位数据")
    CommonResult cBit();
}
