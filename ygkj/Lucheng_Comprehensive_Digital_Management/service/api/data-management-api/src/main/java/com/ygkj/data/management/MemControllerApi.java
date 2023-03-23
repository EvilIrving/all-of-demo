package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.MemReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "內存信息", value = "內存信息")
public interface MemControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(MemReqVo vo);


    @ApiOperation("查找最新的那条数据")
    CommonResult findNewLastData();

}
