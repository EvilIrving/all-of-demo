package com.ygkj.platform.management;

import com.ygkj.platform.management.vo.request.SysRouteLogPage;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "路由调用日志")
public interface SysRouteLogControllerApi {

    @ApiOperation("添加访问记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "url", value = "路由"),
            @ApiImplicitParam(name = "interviewMethod", value = "访问方式：web端/快捷/浙政钉/微信")})
    CommonResult add(String url, String interviewMethod);

    @ApiOperation("历史搜索---访问日志")
    CommonResult pageList(SysRouteLogPage pageVo);

    @ApiOperation("行为日志---历史记录")
    CommonResult routeStatistic(SysRouteLogPage page);
}
