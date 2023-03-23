package com.ygkj.flood;

import com.ygkj.flood.vo.request.AttDictAddVo;
import com.ygkj.flood.vo.request.AttDictRequestVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "数据字典", value = "数据字典")
public interface DictControllerApi {

    @ApiOperation("数据字典新增")
    CommonResult insertDict(AttDictAddVo attDictAddVo);

    @ApiOperation("数据字典查询")
    CommonResult selectDict(AttDictRequestVo requestVo);
}
