package com.ygkj.platform.management;

import com.ygkj.platform.management.model.SysErrorLog;
import com.ygkj.platform.management.vo.request.SysErrorLogPage;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "错误日志控制类")
public interface SysErrorLogControllerApi {

    @ApiOperation("添加错误日志")
    CommonResult add(SysErrorLog addVo);

    @ApiOperation("错误日志分页列表")
    CommonResult pageList(SysErrorLogPage pageVo);

    @ApiOperation("异常访问的折线图")
    CommonResult findErrorDayCounts(String startDay);
}
