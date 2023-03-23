package com.ygkj.visualization;

import com.ygkj.visualization.vo.request.DisasterPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "大屏灾害组件", value = "大屏灾害组件")
public interface DisasterControllerApi {

    @ApiOperation("详表列表")
    CommonResult selectByCondition(DisasterPageQueryVo queryVo);

    @ApiOperation("详细信息")
    CommonResult load(Integer id);

    @ApiOperation("损失最大的五场灾害")
    CommonResult top5Disaster();

    @ApiOperation("详表统计")
    CommonResult statistic();
}
