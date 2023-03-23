package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.CpuReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "cup信息", value = "cup信息")
public interface CpuControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(CpuReqVo vo);


    @ApiOperation("查找最新的那条数据")
    CommonResult findNewLastData();
}
