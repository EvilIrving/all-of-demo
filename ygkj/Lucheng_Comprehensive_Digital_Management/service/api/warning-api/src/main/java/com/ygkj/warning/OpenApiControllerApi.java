package com.ygkj.warning;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "开放api", value = "开放api")
public interface OpenApiControllerApi {

    @ApiOperation("发送政务钉钉提醒")
    CommonResult sendDingDingNotice(String list);
}
