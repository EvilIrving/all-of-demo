package com.ygkj.river;

import com.ygkj.river.vo.request.AttHpapBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "山塘临水线点接口")
public interface AttHpapBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttHpapBaseReqVo vo);

}
