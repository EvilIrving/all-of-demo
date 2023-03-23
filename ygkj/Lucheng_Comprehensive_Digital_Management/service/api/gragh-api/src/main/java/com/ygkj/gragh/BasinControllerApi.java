package com.ygkj.gragh;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "流域信息", value = "流域信息")
public interface BasinControllerApi {

    @ApiOperation(value = "流域列表")
    CommonResult basinList();
}
