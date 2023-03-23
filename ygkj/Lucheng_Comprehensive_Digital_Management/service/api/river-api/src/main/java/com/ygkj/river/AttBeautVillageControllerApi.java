package com.ygkj.river;

import com.ygkj.river.vo.request.AttBeautVillageReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水美乡镇接口")
public interface AttBeautVillageControllerApi {

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttBeautVillageReqVo vo);

}
