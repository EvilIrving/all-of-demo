package com.ygkj.river;

import com.ygkj.river.vo.request.AttHpwlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "山塘水面线接口")
public interface AttHpwlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttHpwlBaseReqVo vo);

}
