package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.RelationStatisticQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "综合分析", value = "综合分析")
public interface DataStatisticControllerApi {

    @ApiOperation("关联分析")
    CommonResult relationStatistic(RelationStatisticQueryVo queryVo);
}
