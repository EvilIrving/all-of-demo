package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.MaxWaterLevelQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "极值报表", value = "极值报表")
public interface MaxValueControllerApi {

    @ApiOperation("极值报表查询，所有查询通用")
    CommonResult maxValues(MaxWaterLevelQueryVo queryVo);
}
