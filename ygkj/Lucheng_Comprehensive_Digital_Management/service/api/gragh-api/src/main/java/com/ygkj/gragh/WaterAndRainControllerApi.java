package com.ygkj.gragh;

import com.ygkj.gragh.vo.request.*;
import com.ygkj.gragh.vo.request.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Api(tags = "水雨工情", value = "水雨工情")
public interface WaterAndRainControllerApi {

    @ApiOperation("水位测站地图点位")
    CommonResult waterStations(SynWaterRequestVo synWaterRequestVo);

    @ApiOperation("水情 超警数量统计")
    CommonResult waterStationsWarningCount();

    @ApiOperation("水位测站地图点位-post body 形势")
    CommonResult waterStationsBody(SynWaterRequestVo synWaterRequestVo);

    @ApiOperation("水位弹窗图表和列表数据")
    CommonResult waterLevels(WaterLevelQueryVo waterLevelQueryVo);

    @ApiOperation("工程主测站的小时水位，主测站的小时雨量对应的数据")
    CommonResult waterRainHours(WaterLevelVo waterLevelVo);

    @ApiOperation("导出水位弹窗图表和列表数据")
    void exportWaterLevels(WaterLevelQueryVo waterLevelQueryVo,HttpServletResponse response);

    @ApiOperation("根据时间区间查询多个测站水雨工情数据")
    CommonResult multiWaterRains(MultiWaterRainReqVo reqVo);

    @ApiOperation("雨量右侧条件查询数据列表")
    CommonResult rainfall(SynRainRequestVo synRainRequestVo);

    @ApiOperation("一张图雨量 + 预警")
    CommonResult rainfallForOne(SynRainRequestVo synRainRequestVo);


    @ApiOperation("一张图 降雨 + 预警 数量")
    CommonResult rainfallForOneNumTotal(SynRainRequestVo synRainRequestVo);


    @ApiOperation("雨量弹窗图标与列表数据")
    CommonResult hourRainfall(HourRainfallQueryVo hourRainfallQueryVo);

    @ApiOperation("雨量弹窗图标与列表数据")
    void exportHourRainfall(HourRainfallQueryVo hourRainfallQueryVo,HttpServletResponse response);

    @ApiOperation("雨量变化曲线")
    CommonResult rainfallCurve(HourRainfallQueryVo queryVo);

    @ApiOperation("未来七天雨量预测")
    @ApiImplicitParams({@ApiImplicitParam(name = "longitude", value = "经度"),
            @ApiImplicitParam(name = "latitude", value = "纬度")})
    CommonResult futureSevenDayRainfallPredict(String longitude, String latitude);

    @ApiOperation("全温州1/3/6/12/24/72小时的降雨总量")
    CommonResult rainfallStatistic(String areaName, String endTime);

    @ApiOperation("全温州1/3/6/12/24/72小时的降雨总量")
    CommonResult rainfallStatistic4Drill(Rainfall4DrillReqVo reqVo);

    @ApiOperation("过去1/3/6/12/24/72小时区县的平均降雨量")
    CommonResult pastHoursRainfall();

    CommonResult grandTotalMaxRainfallStatistic(String endTime, Integer hour);

    CommonResult grandTotalMaxRainfallStatistic4Drill(Rainfall4DrillReqVo reqVo);

    @ApiOperation("累计降雨量统计详表")
    @ApiImplicitParams({@ApiImplicitParam(name = "endTime", value = "时间点，可不传，默认当前时间"),
            @ApiImplicitParam(name = "type", value = "统计类型：只能传面雨量或者点雨量"),
            @ApiImplicitParam(name = "surfaceType", value = "面雨量统计类型：只能传行政区划或者流域"),
            @ApiImplicitParam(name = "hour", value = "小时数，近一小时传0，其他1/3/6/24表示前N小时...")})
    CommonResult grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour);

    @ApiOperation("大中型水库平均面雨量图表")
    @ApiImplicitParams({@ApiImplicitParam(name = "endTime", value = "时间点，可不传，默认当前时间"),
            @ApiImplicitParam(name = "hour", value = "小时")})
    CommonResult grandTotalReservoirRainfall(String endTime, Integer hour);

    @ApiOperation("大中型水库平均面雨量图表演练用")
    CommonResult grandTotalReservoirRainfall4Drill(Rainfall4DrillReqVo reqVo);

    @ApiOperation("查询河网")
    CommonResult listRivernet(RivernetWaterLevelReqVo reqVo);

    @ApiOperation("导出河网")
    void exportRivernet(RivernetWaterLevelReqVo reqVo, HttpServletResponse response);

    @ApiOperation("防御复盘雨量站区间个数统计")
    CommonResult judgeRainfallIntervalStation(String beginTime, String endTime);

    @ApiOperation("防御复盘自定义区间面雨量统计")
    CommonResult customRainfallSurfaceStatistic(String beginTime, String endTime);

    @ApiOperation("防御复盘自定义区间面雨量数据")
    CommonResult customRainfallSurfaceData(String beginTime, String endTime);

    @ApiOperation("河网复盘统计")
    CommonResult rivernetReplayStats(RivernetReplayReqVo reqVo);

    @ApiOperation("潮位复盘统计")
    CommonResult tideReplayStats(TideReplayReqVo reqVo);

    @ApiOperation("市县自建（永嘉数据）")
    CommonResult yongJiaRainStation(SynRainRequestVo vo);

    @ApiOperation("墒情监测测站地图点位")
    CommonResult soilMoistureMonitorStations(SynWaterRequestVo synWaterRequestVo);

    @ApiOperation("墒情监测弹窗列表数据")
    CommonResult soilMoistureMonitorLevels(WaterLevelQueryVo waterLevelQueryVo);

    @ApiOperation("前期影响雨量曲线")
    CommonResult precedeAffectRainfallCurve(String adcd);

    @ApiOperation("前期影响雨量列表,单天全行政区划")
    CommonResult precedeAffectRainfall(Date theDay);

    @ApiOperation("前期影响雨量列表")
    CommonResult pagePrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo);

    @ApiOperation("前期影响雨量列表-详表统计")
    CommonResult stats4PrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo);

    @ApiOperation("前期影响雨量列表-导出")
    void exportPrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo, HttpServletResponse response);

    @ApiOperation("前期影响雨量C位")
    CommonResult precedeAffectRainfall4CWindow();

    @ApiOperation("获取指定测站历史上1、3、6、24、72小时最大的累计雨量值")
    CommonResult rainfallHistoryMax(String stationCode);
}
