package com.ygkj.flood;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

/**
 * @author xq
 * @Description 防汛形势研判-降雨分析Api
 * @Date 2021/1/18
 */
@Api(tags = "防汛研判-降雨分析")
public interface RainfallAnalysisControllerApi {

    @ApiOperation("各行政区划测站的实时累计雨量")
    CommonResult getAllAdcdRealtimeRainfall(Integer minutes, String adnm);

    @ApiOperation("各流域测站的实时累计雨量")
    CommonResult getAllBasinRealtimeRainfall(Integer minutes, String basin);
//
//    @ApiOperation("温州实时雨量统计-饼图，不用做了")
//    CommonResult getRealtimeRainLevelStatistic(Integer minutes);
//
//    /**
//     * 宋亚路方案还未确定，直接创建极值表，查极值表中的数据
//     */
//    @ApiOperation("温州雨量历史极值")
//    CommonResult getRainfallExtremum(Integer minutes,String stName);

    @ApiOperation("温州降雨量报警")
    CommonResult getRainWarningStationNum(Integer hours);

    @ApiOperation("查询温州雨量站极值")
    CommonResult getHistoricRainfallExtremum();

    @ApiOperation("查询各个雨量站累计降雨值")
    CommonResult getPpStNumStatistic(@ApiParam("分钟数，如1小时传60") Integer minutes);

    @ApiOperation("查询重点关注的预报雨量")
    CommonResult getCollectPointRainfall();

    @ApiOperation("查询水库的累计雨量")
    CommonResult getResPointRainfall();


}
