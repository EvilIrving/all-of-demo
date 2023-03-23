package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.DiskReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "磁盘信息", value = "磁盘信息")
public interface DiskControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(DiskReqVo vo);


    @ApiOperation("查找最新的那条数据")
    CommonResult findNewLastData();

}
