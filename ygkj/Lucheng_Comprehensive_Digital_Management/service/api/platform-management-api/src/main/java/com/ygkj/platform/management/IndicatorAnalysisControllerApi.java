package com.ygkj.platform.management;

import com.ygkj.platform.management.vo.request.IndicatorAnalysisPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "指标管理-----指标分析", value = "指标管理-----指标分析")
public interface IndicatorAnalysisControllerApi {
    @ApiOperation("列表")
    CommonResult analyses(IndicatorAnalysisPageQueryVo queryVo);
}
