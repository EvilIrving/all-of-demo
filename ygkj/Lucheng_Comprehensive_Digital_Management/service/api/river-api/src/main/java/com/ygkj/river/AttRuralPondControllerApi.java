package com.ygkj.river;

import com.ygkj.river.vo.request.AttRuralPondReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "农村池塘接口")
public interface AttRuralPondControllerApi {

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttRuralPondReqVo vo);

}
