package com.ygkj.river;

import com.ygkj.river.vo.request.AttHpmlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "山塘管理范围线接口")
public interface AttHpmlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttHpmlBaseReqVo vo);

}
