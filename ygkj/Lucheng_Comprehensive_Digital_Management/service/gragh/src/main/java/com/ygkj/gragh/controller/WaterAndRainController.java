package com.ygkj.gragh.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.WaterAndRainControllerApi;
import com.ygkj.gragh.enums.StationType;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.gragh.model.YongjiaMetaData;
import com.ygkj.gragh.service.PrecedeAffectRainfallService;
import com.ygkj.gragh.service.RivernetService;
import com.ygkj.gragh.service.SynWaterAndRainService;
import com.ygkj.gragh.service.YongjiaSynchronizedService;
import com.ygkj.gragh.vo.request.*;
import com.ygkj.gragh.vo.response.StSoliRRespVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/waterAndRain")
public class WaterAndRainController implements WaterAndRainControllerApi {

    @Autowired
    private SynWaterAndRainService waterAndRainService;

    @Autowired
    private YongjiaSynchronizedService synchronizedService;

    @Autowired
    private RivernetService rivernetService;

    @Autowired
    private PrecedeAffectRainfallService precedeAffectRainfallService;

    @PostMapping("/waterStations")
    @Override
    public CommonResult waterStations(SynWaterRequestVo synWaterRequestVo) {
        return CommonResult.success(waterAndRainService.stations(synWaterRequestVo));
    }


    @Override
    @GetMapping("/waterStationsWarningCount")
    public CommonResult waterStationsWarningCount() {
        SynWaterRequestVo vo = new SynWaterRequestVo();
        vo.setStationType(StationType.RESERVOIR.type());
        vo.setWarning(true);
        List<AttStBase> rrStations = waterAndRainService.stations(vo);
        JSONObject result = new JSONObject();
        result.put("水库水情", CollectionUtils.isNotBlank(rrStations)? rrStations.size() : 0);
        vo.setStationType(StationType.RIVER.type());
        List<AttStBase> zzStations = waterAndRainService.stations(vo);
        result.put("江河水情", CollectionUtils.isNotBlank(zzStations)? zzStations.size() : 0);
        vo.setStationType(StationType.TIDE.type());
        List<AttStBase> ttStations = waterAndRainService.stations(vo);
        result.put("潮位水情", CollectionUtils.isNotBlank(ttStations)? ttStations.size() : 0);
        return CommonResult.success(result);
    }

    @PostMapping("/waterStationsBody")
    @Override
    public CommonResult waterStationsBody(@RequestBody SynWaterRequestVo synWaterRequestVo) {
        return CommonResult.success(waterAndRainService.stations(synWaterRequestVo));
    }

    @PostMapping("/waterLevels")
    @Override
    public CommonResult waterLevels(WaterLevelQueryVo waterLevelQueryVo) {
        return CommonResult.success(waterAndRainService.waterLevel(waterLevelQueryVo));
    }

    @PostMapping("/waterRainHours")
    @Override
    public CommonResult waterRainHours(WaterLevelVo waterLevelVo) {
        return CommonResult.success(waterAndRainService.waterRainHours(waterLevelVo));
    }

    @PostMapping("/waterLevelsForFeign")
    public CommonResult waterLevelsForFeign(@RequestBody WaterLevelQueryVo waterLevelQueryVo) {
        return CommonResult.success(waterAndRainService.waterLevel(waterLevelQueryVo));
    }

    @PostMapping("/exportWaterLevels")
    @Override
    public void exportWaterLevels(@RequestBody WaterLevelQueryVo waterLevelQueryVo,HttpServletResponse response) {
        waterAndRainService.exportWaterLevels(waterLevelQueryVo,response);
    }

    @PostMapping("/multiWaterRains")
    @Override
    public CommonResult multiWaterRains(@RequestBody MultiWaterRainReqVo reqVo) {
        return CommonResult.success(waterAndRainService.multiWaterRains(reqVo));
    }

    @PostMapping("/rainfall")
    @Override
    public CommonResult rainfall(SynRainRequestVo synRainRequestVo) {
        return CommonResult.success(waterAndRainService.rainfall(synRainRequestVo));
    }


    @Override
    @PostMapping("/rainfallForOne")
    public CommonResult rainfallForOne(SynRainRequestVo synRainRequestVo) {
        return waterAndRainService.rainfallForOne(synRainRequestVo);
    }


    @Override
    @PostMapping("/rainfallNum")
    public CommonResult rainfallForOneNumTotal(SynRainRequestVo synRainRequestVo) {
        return waterAndRainService.rainfallForOneNum(synRainRequestVo);
    }

    @PostMapping("/hourRainfall")
    @Override
    public CommonResult hourRainfall(HourRainfallQueryVo hourRainfallQueryVo) {
        return CommonResult.success(waterAndRainService.hourRainfall(hourRainfallQueryVo));
    }

    @Override
    @PostMapping("/exportHourRainfall")
    public void exportHourRainfall(@RequestBody HourRainfallQueryVo hourRainfallQueryVo, HttpServletResponse response) {
        waterAndRainService.exportHourRainfall(hourRainfallQueryVo,response);
    }

    @PostMapping("/rainfallCurve")
    @Override
    public CommonResult rainfallCurve(HourRainfallQueryVo queryVo) {
        return CommonResult.success(waterAndRainService.rainfallCurve(queryVo));
    }

    @PostMapping("/futureSevenDayRainfallPredict")
    @Override
    public CommonResult futureSevenDayRainfallPredict(String longitude, String latitude) {
        return CommonResult.success(waterAndRainService.futureSevenDayRainfallPredict(longitude, latitude));
    }

    @PostMapping("/synchroniedYongjiaData")
    public CommonResult synchroniedYongjiaData(YongjiaMetaData metaData) {
        if (synchronizedService.synchronizeData(metaData)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PostMapping("/rainfallStatistic")
    @Override
    public CommonResult rainfallStatistic(String areaName, String endTime) {
        return CommonResult.success(waterAndRainService.rainfallStatistic(areaName, endTime));
    }

    @PostMapping("/rainfallStatistic4Drill")
    @Override
    public CommonResult rainfallStatistic4Drill(@RequestBody Rainfall4DrillReqVo reqVo) {
        return CommonResult.success(waterAndRainService.rainfallStatistic4Drill(reqVo.getStcds(), reqVo.getStartTime(), reqVo.getEndTime()));
    }

    @GetMapping("/pastHoursRainfall")
    @Override
    public CommonResult pastHoursRainfall() {
        return CommonResult.success(waterAndRainService.pastHoursRainfall());
    }


    @PostMapping("/grandTotalMaxRainfallStatistic")
    @Override
    public CommonResult grandTotalMaxRainfallStatistic(String endTime, Integer hour) {
        return CommonResult.success(waterAndRainService.grandTotalMaxRainfallStatistic(endTime, hour));
    }

    @PostMapping("/grandTotalMaxRainfallStatistic4Drill")
    @Override
    public CommonResult grandTotalMaxRainfallStatistic4Drill(@RequestBody Rainfall4DrillReqVo reqVo) {
        return CommonResult.success(waterAndRainService.grandTotalMaxRainfallStatistic4Drill(reqVo.getStcds(), reqVo.getStartTime(), reqVo.getEndTime(), reqVo.getHour()));
    }

    @PostMapping("/grandTotalRainfallGrid")
    @Override
    public CommonResult grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour) {
        return CommonResult.success(waterAndRainService.grandTotalRainfallGrid(requestVo, endTime, type, surfaceType, hour));
    }

    @PostMapping("/grandTotalReservoirRainfall")
    @Override
    public CommonResult grandTotalReservoirRainfall(String endTime, Integer hour) {
        return CommonResult.success(waterAndRainService.grandTotalReservoirRainfall(endTime, hour));
    }

    @PostMapping("/grandTotalReservoirRainfall4Drill")
    @Override
    public CommonResult grandTotalReservoirRainfall4Drill(@RequestBody Rainfall4DrillReqVo reqVo) {
        return CommonResult.success(waterAndRainService.grandTotalReservoirRainfall4Drill(reqVo.getStcds(), reqVo.getStartTime(), reqVo.getEndTime(), reqVo.getHour()));
    }

    @PostMapping("/listRivernet")
    @Override
    public CommonResult listRivernet(RivernetWaterLevelReqVo reqVo) {
        return CommonResult.success(rivernetService.listRivernet(reqVo));
    }

    @PostMapping("/exportRivernet")
    @Override
    public void exportRivernet(RivernetWaterLevelReqVo reqVo, HttpServletResponse response) {
        rivernetService.exportRivernet(reqVo, response);
    }

    @PostMapping("/judgeRainfallIntervalStation")
    @Override
    public CommonResult judgeRainfallIntervalStation(String beginTime, String endTime) {
        return CommonResult.success(waterAndRainService.judgeRainfallStation(beginTime, endTime));
    }

    @PostMapping("/customRainfallSurfaceStatistic")
    @Override
    public CommonResult customRainfallSurfaceStatistic(String beginTime, String endTime) {
        return CommonResult.success(waterAndRainService.customRainfallSurfaceStatistic(beginTime, endTime));
    }

    @PostMapping("/customRainfallSurfaceData")
    @Override
    public CommonResult customRainfallSurfaceData(String beginTime, String endTime) {
        return CommonResult.success(waterAndRainService.customRainfallSurfaceData(beginTime, endTime));
    }

    @GetMapping("/rivernetReplayStats")
    @Override
    public CommonResult rivernetReplayStats(RivernetReplayReqVo reqVo) {
        return CommonResult.success(rivernetService.rivernetReplayStats(reqVo));
    }

    @GetMapping("/tideReplayStats")
    @Override
    public CommonResult tideReplayStats(TideReplayReqVo reqVo) {
        return CommonResult.success(waterAndRainService.tideReplayStats(reqVo));
    }

    @PostMapping("/yongJiaRainStation")
    @Override
    public CommonResult yongJiaRainStation(SynRainRequestVo vo) {
        return CommonResult.success(waterAndRainService.yongJiaRainStation(vo));
    }

    @PostMapping("soilMoistureMonitorStations")
    @Override
    public CommonResult soilMoistureMonitorStations(SynWaterRequestVo vo) {
        return CommonResult.success(waterAndRainService.soilMoistureMonitorStations(vo));
    }

    @PostMapping("soilMoistureMonitorLevels")
    @Override
    public CommonResult<List<StSoliRRespVo>> soilMoistureMonitorLevels(WaterLevelQueryVo vo) {
        return CommonResult.success(waterAndRainService.soilMoistureMonitorLevels(vo));
    }

    @GetMapping("precedeAffectRainfallCurve")
    @Override
    public CommonResult precedeAffectRainfallCurve(String adcd) {
        return CommonResult.success(precedeAffectRainfallService.precedeAffectRainfallCurve(adcd));
    }

    @GetMapping("precedeAffectRainfall")
    @Override
    public CommonResult precedeAffectRainfall(@RequestParam(value = "theDay", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date theDay) {
        return CommonResult.success(precedeAffectRainfallService.todayPrecedeAffectRainfall(theDay));
    }

    @GetMapping("pagePrecedeAffectRainfall")
    @Override
    public CommonResult pagePrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo) {
        return CommonResult.success(precedeAffectRainfallService.pagePrecedeAffectRainfall(queryVo));
    }

    @GetMapping("stats4PrecedeAffectRainfall")
    @Override
    public CommonResult stats4PrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo) {
        return CommonResult.success(precedeAffectRainfallService.stats4PrecedeAffectRainfall(queryVo));
    }

    @PostMapping("exportPrecedeAffectRainfall")
    @Override
    public void exportPrecedeAffectRainfall(@RequestBody PrecedeAffectRainfallQueryVo queryVo, HttpServletResponse response) {
        precedeAffectRainfallService.exportPrecedeAffectRainfall(queryVo, response);
    }

    @GetMapping("precedeAffectRainfall4CWindow")
    @Override
    public CommonResult precedeAffectRainfall4CWindow() {
        return CommonResult.success(precedeAffectRainfallService.precedeAffectRainfall4CWindow());
    }

    @PostMapping("/rainfallHistoryMax")
    @Override
    public CommonResult rainfallHistoryMax(String stationCode) {
        return CommonResult.success(waterAndRainService.rainfallHistoryMax(stationCode));
    }


    @GetMapping("rainfall24/{stCode}")
    public CommonResult rainfall24(@PathVariable("stCode") String stCode){
        return CommonResult.success(waterAndRainService.rainfall24(stCode));
    }

    @PostMapping("/selectTideByTimeCode")
    public List<StTideR> selectTideByTimeCode(@RequestBody SelectTideByTimeCodeVo selectTideByTimeCodeVo) {
        return waterAndRainService.selectTideByTimeCode(selectTideByTimeCodeVo.getTable(),selectTideByTimeCodeVo.getStcds(),selectTideByTimeCodeVo.getDataTime());
    }

    @ApiOperation("降雨量等值面")
    @GetMapping("/rainfallSurface")
    public CommonResult rainfallSurface(String dataInterval, String startTime, String endTime) {
        return CommonResult.success(waterAndRainService.calculateRainfallSurface(dataInterval, startTime, endTime));
    }
}
