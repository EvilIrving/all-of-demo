package com.ygkj.platform.management;

import com.ygkj.platform.management.model.SysQueryLog;
import com.ygkj.platform.management.vo.request.SysQueryLogPage;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "查询调用日志")
public interface SysQueryLogControllerApi {

    @ApiOperation("添加查询接口的调用记录")
    CommonResult add(SysQueryLog addVo);

    @ApiOperation("查询接口的调用记录分页列表")
    CommonResult pageList(SysQueryLogPage pageVo);

    @ApiOperation("最热门的关键词")
    CommonResult topKeyWord(Integer limit);

}
