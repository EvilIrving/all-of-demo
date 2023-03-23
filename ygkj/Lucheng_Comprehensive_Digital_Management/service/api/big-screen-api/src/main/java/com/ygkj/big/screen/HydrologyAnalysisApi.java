package com.ygkj.big.screen;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/24
 */
@Api(tags = "水文分析")
public interface HydrologyAnalysisApi {
    @ApiOperation("报警统计")
    List<JSONObject> warnStatistic();

    @ApiOperation("潮位极值：type=1(最大值)/type=2(最小值)")
    List<JSONObject> tideExtreme(Integer type);

    @ApiOperation("水位分析：type=1(大中型水库)/type=2(流域水位)/type=3(平原水位)")
    List<JSONObject> waterLevelAnalysis(Integer type);

    @ApiOperation("降雨极值")
    List<JSONObject> rainfallExtreme();

    @ApiOperation("水位极值")
    List<JSONObject> waterLevelExtreme();


}