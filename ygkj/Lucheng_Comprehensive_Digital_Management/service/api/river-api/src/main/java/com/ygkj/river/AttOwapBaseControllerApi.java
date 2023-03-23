package com.ygkj.river;

import com.ygkj.river.vo.request.AttOwapBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "其他水域临水线点接口")
public interface AttOwapBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttOwapBaseReqVo vo);

}
