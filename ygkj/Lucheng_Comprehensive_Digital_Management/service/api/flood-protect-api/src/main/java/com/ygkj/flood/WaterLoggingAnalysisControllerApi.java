package com.ygkj.flood;

import com.ygkj.flood.vo.request.FloodPointReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Api(tags = "内涝工情分析")
public interface WaterLoggingAnalysisControllerApi {
    @ApiOperation("积水点列表")
    CommonResult floodPoint(FloodPointReqVo reqVo);

    @ApiOperation("积水点 post body 请求")
    CommonResult listFloodPoint(FloodPointReqVo reqVo);

    @ApiOperation("积水点分页")
    CommonResult pageFloodPoint(FloodPointReqVo reqVo);

    @ApiOperation("积水点分页")
    CommonResult stats4PageFloodPoint(FloodPointReqVo reqVo);

    @ApiOperation("积水点导出")
    void exportFloodPoint(FloodPointReqVo reqVo, HttpServletResponse response);

    @ApiOperation("积水点C位统计")
    CommonResult floodPointCWindows(Date dataTime);

    @ApiOperation("积水点积水次数热力图")
    CommonResult floodPointHeatMap(Date dataTime);

    @ApiOperation("积水点历史曲线")
    CommonResult floodPointHistoricCurve(String name, Integer hours);

    @ApiOperation("查询最深的几个积水点位历史积水曲线")
    CommonResult floodPointCurveList(Integer limit);

    @ApiOperation("内涝模型")
    CommonResult floodPointForecastCurve(String name);


    @ApiOperation("积水历史和预测曲线")
    CommonResult floodPointHistoricForecastCurve(String name);


    @ApiOperation("新积水历史和预测曲线")
    CommonResult waterLoggingHistoricForecastCurve(String name, Integer hours);

    /**
     * what hell have designers did？
     * they relate waterlogging point with basin。
     * do they have no idea about statistic ？
     * incredible！！！
     * they must be god send to punish me for what my previous life did。
     *
     * @return
     */
    @ApiOperation("积水点统计")
    CommonResult floodPointStatistic(@ApiParam("1. 根据行政区划统计 " +
            "2. 根据流域统计 " +
            "3. 统计无数据、无积水、有积水的点位数量" +
            "4. 根据行政区划统计有积水/无积水的点位数量" +
            "5. 根据流域统计有积水/无积水点位的数量") int type);

    @ApiOperation("河网水位")
    CommonResult riverWaterLevel(String basin);

    @ApiOperation("水闸")
    CommonResult wagaList(String basin);

    @ApiOperation("洪水风险")
    CommonResult floodRisk();

    @ApiOperation("报警统计")
    CommonResult warnNum();
}
