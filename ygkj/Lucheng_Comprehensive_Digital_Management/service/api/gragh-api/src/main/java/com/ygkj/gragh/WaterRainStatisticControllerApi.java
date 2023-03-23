package com.ygkj.gragh;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author xq
 * @Date 2021/2/23
 */
@Api(tags = "水雨工情统计")
public interface WaterRainStatisticControllerApi {
    @ApiOperation("根据行政区划统计")
    CommonResult statisticStationNumByAdcd(@ApiParam("RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道,多个类型用逗号,隔开"
    ) String stType);


    @ApiOperation("根据行政区划流域统计各测站类型数量")
    CommonResult countStTypeNumByAdcdBasin(String adcd, String basin);
}
