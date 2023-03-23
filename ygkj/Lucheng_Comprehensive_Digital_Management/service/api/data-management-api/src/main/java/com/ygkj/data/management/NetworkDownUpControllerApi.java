package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.NetworkDownUpReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "网络io信息", value = "网络io信息")
public interface NetworkDownUpControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(NetworkDownUpReqVo vo);


    @ApiOperation("查找最新的那条数据")
    CommonResult findNewLastData();

}
