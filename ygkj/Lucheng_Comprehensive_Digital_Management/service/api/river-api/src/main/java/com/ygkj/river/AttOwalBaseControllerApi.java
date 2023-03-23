package com.ygkj.river;

import com.ygkj.river.vo.request.AttOwalBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "其他水域临水线接口")
public interface AttOwalBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttOwalBaseReqVo vo);

}
