package com.ygkj.digitization.controller;

import com.ygkj.digitization.SynchronizedStatisticControllerApi;
import com.ygkj.digitization.service.RainAndWaterService;
import com.ygkj.digitization.vo.request.CwsReqVo;
import com.ygkj.digitization.vo.request.ProjectListReqVo;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.digitization.vo.response.GateStationResVo;
import com.ygkj.digitization.vo.response.RainfallStationExportVo;
import com.ygkj.digitization.vo.response.RainfallStationResVo;
import com.ygkj.digitization.vo.response.WaterLevelStationResVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/synchronizedStatistic")
public class SynchronizedStatisticController implements SynchronizedStatisticControllerApi {

    @Autowired
    private RainAndWaterService rainAndWaterService;

    @PostMapping("/synchronizedRainfall")
    @Override
    public CommonResult synchronizedRainfall(StationQueryVo queryVo) {
        return CommonResult.success(rainAndWaterService.rainStationSynchronizedStatistic(queryVo));
    }

    @PostMapping("/synchronizedWaterLevel")
    @Override
    public CommonResult synchronizedWaterLevel(StationQueryVo queryVo) {
        return CommonResult.success(rainAndWaterService.waterLevelStationSynchronizedStatistic(queryVo));
    }
    @PostMapping("/synchronizedGate")
    @Override
    public CommonResult synchronizedGate(StationQueryVo queryVo) {
        return CommonResult.success(rainAndWaterService.gateStationSynchronizedStatistic(queryVo));
    }

    @PostMapping("listCws")
    @Override
    public CommonResult listCws(ProjectListReqVo reqVo) {
        return CommonResult.success(rainAndWaterService.listCws(reqVo));
    }

    @PostMapping("listCwsDetail")
    @Override
    public CommonResult listCwsDetail(CwsReqVo reqVo) {
        return CommonResult.success(rainAndWaterService.listCwsDetail(reqVo));
    }

    @Override
    @PostMapping("/exportRainfall")
    public void exportRainfall(StationQueryVo vo, HttpServletResponse response) throws Exception {

        List<RainfallStationExportVo> exportVoList = rainAndWaterService.exportRainfallForList(vo);

        ExportUtils.exportExcel(exportVoList,
                "实时雨量列表"+ DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"测站名称","行政区划","01时","02时","03时","04时","05时","06时","07时","08时","09时","10时","11时","12时","13时","14时","15时","16时","17时","18时","19时","20时","21时","22时","23时","24时"},
                new String[]{"stName","areaName","hour01","hour02","hour03","hour04","hour05","hour06","hour07","hour08","hour09","hour10","hour11","hour12","hour13","hour14","hour15","hour16","hour17","hour18","hour19","hour20","hour21","hour22","hour23","hour24"},
                response,RainfallStationExportVo.class);

    }

    @Override
    @PostMapping("/exportWaterLevel")
    public void exportWaterLevel(StationQueryVo vo, HttpServletResponse response) {
        List<WaterLevelStationResVo> list = rainAndWaterService.waterLevelStationSynchronizedStatistic(vo);
        ExportUtils.exportExcel(list,
                "实时水位列表"+ DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"名称","行政区划","上报时间","实时水位","台汛限水位（m）","梅汛限水位","防洪高水位（m）","正常水位（m）","集雨面积","保证水位","警戒水位"},
                new String[]{"stName","areaName","tm","waterLevels","yphoonFloodLimitWaterLevel","meiFloodLimitWaterLevel","uppLevFlco","normWatLev","watShedArea","guaranteeLevel","warningLevel"},
                response,WaterLevelStationResVo.class);
    }

    @Override
    @PostMapping("/exportGate")
    public void exportGate(StationQueryVo vo, HttpServletResponse response) {
        List<GateStationResVo> list = rainAndWaterService.gateStationSynchronizedStatistic(vo);
        ExportUtils.exportExcel(list,
                "实时工期列表"+ DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"测站名称","行政区划","时间","闸下水位（m）","保证水位（m）","闸下开度（m）","过闸流量（m）","闸上水位（m）","警戒水位（m）"},
                new String[]{"name","areaName","tm","downWater","guaranteeLevel","openGate","tgtq","upWater","warningLevel"},
                response,GateStationResVo.class);
    }
}
