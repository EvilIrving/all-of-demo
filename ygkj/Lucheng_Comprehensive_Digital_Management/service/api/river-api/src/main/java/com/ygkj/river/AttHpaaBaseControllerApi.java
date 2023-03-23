package com.ygkj.river;

import com.ygkj.river.vo.request.AttHpaaBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "山塘水域面接口")
public interface AttHpaaBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttHpaaBaseReqVo vo);

}
