package com.ygkj.river;

import com.ygkj.river.vo.request.AttSmallMediumRiverReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "中小河流接口")
public interface AttSmallMediumRiverControllerApi {

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttSmallMediumRiverReqVo vo);

}
