package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.BusApiBasePageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "大屏组件---数据服务", value = "大屏组件---数据服务")
public interface DataServiceControllerApi {

    @ApiOperation("数据申请排名")
    CommonResult applyRank();

    @ApiOperation("数据api请求")
    CommonResult invokeStatistic();

    @ApiOperation("最新上线服务")
    CommonResult newestApi();

    @ApiOperation("服务调用次数")
    CommonResult invokeNumber();

    @ApiOperation("c位数据")
    CommonResult cBitData();

    @ApiOperation("详表")
    CommonResult form(BusApiBasePageQueryVo queryVo);
}
