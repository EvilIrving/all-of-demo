package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WaterDisasterPreventionControllerApi;
import com.ygkj.big.screen.service.WaterDisasterPreventionService;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.CurTyphoonReqVo;
import com.ygkj.big.screen.vo.response.Villages;
import com.ygkj.flood.vo.request.AttTeamBasePageQueryVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.request.SeawallWarnReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/waterDisasterPrevention")
public class WaterDisasterPreventionController implements WaterDisasterPreventionControllerApi {

    private WaterDisasterPreventionService waterDisasterPreventionService;

    public WaterDisasterPreventionController(@Autowired WaterDisasterPreventionService waterDisasterPreventionService) {
        this.waterDisasterPreventionService = waterDisasterPreventionService;
    }

    @GetMapping("/weatherWarning")
    @Override
    public CommonResult weatherWarning() {
        return CommonResult.success(waterDisasterPreventionService.weatherWarning());
    }

    @GetMapping("/rsvrProjStats1")
    @Override
    public CommonResult rsvrProjStats1(@RequestParam(value = "dataTime", required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime,
                                       @RequestParam(value = "drillAffairId", required = false) String drillAffairId) {
        return CommonResult.success(waterDisasterPreventionService.rsvrProjStats1(dataTime, drillAffairId));
    }

    @GetMapping("/weatherPredict")
    @Override
    public CommonResult weatherPredict(String areaName) {
        return waterDisasterPreventionService.weatherPredict(areaName);
    }

    @PostMapping("/predictRainfallByAreaName")
    @Override
    public CommonResult predictRainfallByAreaName(String areaName) {
        return waterDisasterPreventionService.predictRainfallByAreaName(areaName);
    }

    @GetMapping("/rsvrProjStats2")
    @Override
    public CommonResult rsvrProjStats2(@RequestParam("scaleType") String scaleType,
                                       @RequestParam(value = "dataTime", required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime,
                                       @RequestParam(value = "drillAffairId", required = false) String drillAffairId) {
        return CommonResult.success(waterDisasterPreventionService.rsvrProjStats2(scaleType, dataTime, drillAffairId));
    }

    @GetMapping("lmRsvrDrillWlCurve")
    @Override
    public CommonResult lmRsvrDrillWlCurve(@RequestParam("resCode") String resCode,
                                           @RequestParam(value = "start", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                                           @RequestParam("end")
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end,
                                           @RequestParam("timeForward") Boolean timeForward,
                                           @RequestParam(value = "drillAffairId", required = false) String drillAffairId) {
        return CommonResult.success(waterDisasterPreventionService.lmRsvrDrillWlCurve(resCode, start, end, timeForward, drillAffairId));
    }

    @GetMapping("/rsvrProjDrainageStats")
    @Override
    public CommonResult rsvrProjDrainageStats(@RequestParam(value = "dataTime", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime) {
        return CommonResult.success(waterDisasterPreventionService.rsvrProjDrainageStats(dataTime));
    }

    @GetMapping("/rsvrProjList")
    @Override
    public CommonResult rsvrProjList(RsvrStationWaterReqVo reqVo) {
        return CommonResult.success(waterDisasterPreventionService.rsvrProjList(reqVo));
    }

    @GetMapping("wagaStaStats")
    @Override
    public CommonResult wagaStaStats(@RequestParam(value = "dataTime", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime,
                                     @RequestParam("hours") Integer hours) {
        return CommonResult.success(this.waterDisasterPreventionService.wagaStaStats(dataTime, hours));
    }

    @GetMapping("wagaProjStats")
    @Override
    public CommonResult wagaProjStats(@RequestParam(value = "dataTime", required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime,
                                      @RequestParam("hours") Integer hours) {
        return CommonResult.success(this.waterDisasterPreventionService.wagaProjStats(dataTime, hours));
    }

    @GetMapping("/wagaProjList")
    @Override
    public CommonResult wagaProjList(WrpWagaQueryReqVo reqVo) {
        return CommonResult.success(this.waterDisasterPreventionService.wagaProjList(reqVo));
    }

    @GetMapping("riverNetworkStaStats")
    @Override
    public CommonResult riverNetworkStats(
            @RequestParam(value = "dataTime", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime,
            @RequestParam(value = "type", required = false, defaultValue = "1") Integer type) {
        return CommonResult.success(this.waterDisasterPreventionService.riverNetStats(dataTime, type));
    }

    @GetMapping("riverNetworkList")
    @Override
    public CommonResult riverNetworkList(RivNetListQueryReqVo reqVo) {
        return CommonResult.success(this.waterDisasterPreventionService.riverNetworkList(reqVo));
    }

    @GetMapping("tideCurve")
    @Override
    public CommonResult tideCurve(@RequestParam("tideStaName") String tideStaName,
                                  @RequestParam(value = "dataTime", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime) {
        return CommonResult.success(this.waterDisasterPreventionService.tideCurve(tideStaName, dataTime));
    }

    @GetMapping("tideList")
    @Override
    public CommonResult tideList(TideListQueryReqVo reqVo) {
        return CommonResult.success(this.waterDisasterPreventionService.tideList(reqVo));
    }

    @GetMapping("astroTideTable")
    @Override
    public CommonResult astroTideTable(@RequestParam("tideStaName") String tideStaName,
                                       @RequestParam(value = "dataTime", required = false)
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime) {
        return CommonResult.success(this.waterDisasterPreventionService.astroTideTable(tideStaName, dataTime));
    }

    @PostMapping("/rainfallMaxValuePredict")
    @Override
    public CommonResult rainfallMaxValuePredict(Integer hour) {
        return waterDisasterPreventionService.rainfallMaxValuePredict(hour);
    }

    @PostMapping("/rainfallPredictGrid")
    @Override
    public CommonResult rainfallPredictGrid(String type) {
        return waterDisasterPreventionService.rainfallPredictGrid(type);
    }

    @PostMapping("/grandTotalMaxRainfallStatistic")
    @Override
    public CommonResult grandTotalMaxRainfallStatistic(String endTime, Integer hour, String drillAffairId) {
        return waterDisasterPreventionService.grandTotalMaxRainfallStatistic(endTime, hour, drillAffairId);
    }

    @PostMapping("/grandTotalRainfallGrid")
    @Override
    public CommonResult grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour) {
        return waterDisasterPreventionService.grandTotalRainfallGrid(requestVo, endTime, type, surfaceType, hour);
    }

    @PostMapping("/grandTotalReservoirRainfall")
    @Override
    public CommonResult grandTotalReservoirRainfall(String endTime, Integer hour, String drillAffairId) {
        return waterDisasterPreventionService.grandTotalReservoirRainfall(endTime, hour, drillAffairId);
    }

    @GetMapping("seawallProjStats")
    @Override
    public CommonResult seawallProjStats() {
        return this.waterDisasterPreventionService.seawallProjStats();
    }

    @GetMapping("seawallProjList")
    @Override
    public CommonResult seawallProjList(SeawallListQueryReqVo reqVo) {
        return this.waterDisasterPreventionService.seawallProjList(reqVo);
    }

    @GetMapping("seawallUnitCWindow")
    @Override
    public CommonResult seawallUnitCWindow(SeawallWarnReqVo reqVo) {
        return this.waterDisasterPreventionService.seawallUnitCWindow(reqVo);
    }

    @GetMapping("seawallUnitWarnCard")
    @Override
    public CommonResult seawallUnitWarnCard(SeawallWarnReqVo reqVo) {
        return this.waterDisasterPreventionService.seawallUnitWarnCard(reqVo);
    }

    @GetMapping("dilapProjStats")
    @Override
    public CommonResult dilapProjStats() {
        return CommonResult.success(this.waterDisasterPreventionService.dilapProjStats());
    }

    @GetMapping("dilapProjList")
    @Override
    public CommonResult dilapProjList(DilapProjReqVo reqVo) {
        return this.waterDisasterPreventionService.dilapProjList(reqVo);
    }

    @GetMapping("projRiskStats")
    @Override
    public CommonResult projRiskStats(@RequestParam("type") String type) {
        return CommonResult.success(this.waterDisasterPreventionService.projRiskStats(type));
    }

    @GetMapping("projRiskList")
    @Override
    public CommonResult projRiskList(ProjRiskReqVo reqVo) {
        return this.waterDisasterPreventionService.projRiskList(reqVo);
    }

    @GetMapping("rightMapPoints")
    @Override
    public CommonResult rightMapPoints(@RequestParam("type") String type,
                                       @RequestParam(value = "dilapProjType", required = false) String dilapProjType) {
        return CommonResult.success(this.waterDisasterPreventionService.rightMapPoints(type, dilapProjType));
    }

    @GetMapping("/wholeWeather")
    @Override
    public CommonResult wholeWeather() {
        return waterDisasterPreventionService.wholeWeather();
    }

    @GetMapping("/floodWarning")
    @Override
    public CommonResult floodWarning(String endTime, Boolean currentFlag, String drillAffairId) {
        return CommonResult.success(waterDisasterPreventionService.floodWarning4NewVersion(endTime, currentFlag, drillAffairId));
    }

    @GetMapping("/mountainTorrentBaseStats")
    @Override
    public CommonResult mountainTorrentBaseStats() {
        return CommonResult.success(waterDisasterPreventionService.mountainTorrentBaseStats());
    }

    @GetMapping("/mountainTorrentCWindow")
    @Override
    public CommonResult mountainTorrentCWindow(Date dataTime) {
        return CommonResult.success(this.waterDisasterPreventionService.mountainTorrentCWindow(dataTime));
    }

    @GetMapping("/mountainTorrentCWindowTable")
    @Override
    public CommonResult mountainTorrentCWindowTable(Date dataTime, Integer warnType) {
        return CommonResult.success(this.waterDisasterPreventionService.mountainTorrentCWindowTable(dataTime, warnType));
    }

    @PostMapping("/batchSendWarnSms")
    @Override
    public CommonResult batchSendWarnSms(@RequestBody MtSendWarnReqVo reqVo) {
        return this.waterDisasterPreventionService.batchSendWarnSms(reqVo);
    }

    @GetMapping("/mountainTorrentWarnMapPoint")
    @Override
    public CommonResult mountainTorrentWarnMapPoint(Date dataTime) {
        return CommonResult.success(this.waterDisasterPreventionService.mountainTorrentWarnMapPoint(dataTime));
    }

    @PostMapping("/singleSendWarnSms")
    @Override
    public CommonResult singleSendWarnSms(@RequestBody MtSendWarnReqVo reqVo) {
        return this.waterDisasterPreventionService.singleSendWarnSms(reqVo);
    }

    @GetMapping("/clearFloodWarning")
    @Override
    public CommonResult clearFloodWarning() {
        return waterDisasterPreventionService.clearFloodWarning();
    }

    @PostMapping("/judgeRainfallIntervalStation")
    @Override
    public CommonResult judgeRainfallIntervalStation(String beginTime, String endTime) {
        return waterDisasterPreventionService.judgeRainfallIntervalStation(beginTime, endTime);
    }

    @PostMapping("/customRainfallSurfaceStatistic")
    @Override
    public CommonResult customRainfallSurfaceStatistic(String beginTime, String endTime) {
        return waterDisasterPreventionService.customRainfallSurfaceStatistic(beginTime, endTime);
    }

    @PostMapping("/customRainfallSurfaceData")
    @Override
    public CommonResult customRainfallSurfaceData(String beginTime, String endTime) {
        return waterDisasterPreventionService.customRainfallSurfaceData(beginTime, endTime);
    }

    @GetMapping("/rsvrReplayStats")
    @Override
    public CommonResult rsvrReplayStats(RsvrReplayReqVo reqVo) {
        return waterDisasterPreventionService.rsvrReplayStats(reqVo);
    }

    @GetMapping("/wrpWagaReplayStats")
    @Override
    public CommonResult wrpWagaReplayStats(WagaReplayReqVo reqVo) {
        return waterDisasterPreventionService.wrpWagaReplayStats(reqVo);
    }

    @GetMapping("/rivernetReplayStats")
    @Override
    public CommonResult rivernetReplayStats(RivernetReplayReqVo reqVo) {
        return waterDisasterPreventionService.rivernetReplayStats(reqVo);
    }

    @GetMapping("/tideReplayCurve")
    @Override
    public CommonResult tideReplayCurve(@RequestParam("tideStaName") String tideStaName,
                                        @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
                                        @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
        return CommonResult.success(waterDisasterPreventionService.tideReplayCurve(tideStaName, start, end));
    }

    @GetMapping("/tideReplayStats")
    @Override
    public CommonResult tideReplayStats(TideReplayReqVo reqVo) {
        return CommonResult.success(waterDisasterPreventionService.tideReplayStats(reqVo));
    }

    @GetMapping("/warnMapPoints")
    @Override
    public CommonResult warnMapPoints(@RequestParam(value = "type", required = false) String type,
                                      String endTime, String drillAffairId) {
        return CommonResult.success(this.waterDisasterPreventionService.warnMapPoints(type, endTime, drillAffairId));
    }

    @GetMapping("/warnMapPointStats")
    @Override
    public CommonResult warnMapPointStats() {
        return CommonResult.success(this.waterDisasterPreventionService.warnMapPointStats());
    }

    @PostMapping("/exportWarnMapPoint")
    @Override
    public void exportWarnMapPoint(HttpServletResponse response) {
        this.waterDisasterPreventionService.exportWarnMapPoint(response);
    }

    @PostMapping("flashFloodWarnList")
    @Override
    public CommonResult<List<Villages>> flashFloodWarnList(String endTime, Boolean currentFlag, String drillAffairId, String floodType) {
        return CommonResult.success(waterDisasterPreventionService.flashFloodWarnList(endTime, currentFlag, drillAffairId, floodType));
    }

    @GetMapping("/hydrProjStaDiagram")
    @Override
    public CommonResult hydrProjStaDiagram(HydrDiagReqVo reqVo) {
        return this.waterDisasterPreventionService.hydrProjStaDiagram(reqVo);
    }

    @GetMapping("/hydrProjStaDiagramStatistic")
    @Override
    public CommonResult hydrProjStaDiagramStatistic(Integer hours) {
        return this.waterDisasterPreventionService.hydrProjStaDiagramStatistic(hours);
    }

    @PostMapping("rainstormDrillStatistics")
    @Override
    public CommonResult rainstormDrillStatistics(String startTime, String endTime, String drillAffairId) throws ParseException {
        return CommonResult.success(this.waterDisasterPreventionService.rainstormDrillStatistics(startTime, endTime, drillAffairId));
    }

    @PostMapping("rainstormDrillWarn")
    @Override
    public CommonResult rainstormDrillWarn(String endTime) throws ParseException {
        return CommonResult.success(waterDisasterPreventionService.rainstormDrillWarn(endTime));
    }

    @PostMapping("flashFloodDetails")
    @Override
    public CommonResult flashFloodDetails(String nvi, String avi,
                                          @RequestParam(value = "endTime", required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                          String drillAffairId) {
        return CommonResult.success(waterDisasterPreventionService.flashFloodDetails(nvi, avi, endTime, drillAffairId));
    }

    @Deprecated
    @PostMapping("updateDrillRainContourGisImg")
    @Override
    public Object updateDrillRainContourGisImg(@RequestBody DrillRainContourGisImgReqVo reqVo) {
        return waterDisasterPreventionService.updateDrillRainContourGisImg(reqVo);
    }

    @PostMapping("seawallWarnStatistic")
    @Override
    public CommonResult seawallWarnStatistic(@RequestBody SeawallWarnStatisticReqVo reqVo) {
        return waterDisasterPreventionService.seawallWarnStatistic(reqVo);
    }

    @PostMapping("tideWarnStatistic")
    @Override
    public CommonResult tideWarnStatistic() {
        return CommonResult.success(waterDisasterPreventionService.tideWarnStatistic());
    }

    @PostMapping("mountainTorrentDrill")
    @Override
    public CommonResult mountainTorrentDrill(@RequestBody MountainTorrentDrillReqVo reqVo) {
        return CommonResult.success(waterDisasterPreventionService.mountainTorrentDrill(reqVo));
    }

    @GetMapping("floodPointCWindows")
    @Override
    public CommonResult floodPointCWindows(@RequestParam(value = "dataTime", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime) {
        return this.waterDisasterPreventionService.floodPointCWindows(dataTime);
    }

    @GetMapping("waterLoggingStats")
    @Override
    public CommonResult waterLoggingStats(Integer statsType) {
        return CommonResult.success(waterDisasterPreventionService.waterLoggingStats(statsType));
    }

    @GetMapping("waterLoggingList")
    @Override
    public CommonResult waterLoggingList(String areaCode, Boolean ponding, String name, String orderColumn, Boolean ascOrDesc) {
        return waterDisasterPreventionService.waterLoggingList(areaCode, ponding, name, orderColumn, ascOrDesc);
    }

    @GetMapping("waterFlowList")
    @Override
    public CommonResult waterFlowList() {
        return waterDisasterPreventionService.waterFlowList();
    }

    @GetMapping("waterFlowStats")
    @Override
    public CommonResult waterFlowStats() {
        return waterDisasterPreventionService.waterFlowStats();
    }

    @GetMapping("typhoonStats4C")
    @Override
    public CommonResult typhoonStats4C(CurTyphoonReqVo reqVo) {
        return waterDisasterPreventionService.typhoonStats4C(reqVo);
    }

    @GetMapping("listCamera")
    @Override
    public CommonResult listCamera(String cameraName, String areaCode, String projectType, String status, String orderColumn, Boolean ascOrDesc) {
        return waterDisasterPreventionService.listCamera(Builder.of(HikCameraQueryReqVo::new)
                .with(HikCameraQueryReqVo::setCameraName, cameraName)
                .with(HikCameraQueryReqVo::setAreaCode, areaCode)
                .with(HikCameraQueryReqVo::setStatus, status)
                .with(HikCameraQueryReqVo::setProjectType, projectType)
                .with(HikCameraQueryReqVo::setOrderColumn, orderColumn)
                .with(HikCameraQueryReqVo::setAscOrDesc, ascOrDesc)
                .build());
    }

    @PostMapping("listTeam")
    @Override
    public CommonResult listTeam(String areaCode, String typeId, String name, String orderColumn, Boolean ascOrDesc) {
        return waterDisasterPreventionService.listTeam(Builder.of(AttTeamBasePageQueryVo::new)
                .with(AttTeamBasePageQueryVo::setAreaCode, areaCode)
                .with(AttTeamBasePageQueryVo::setTypeId, typeId)
                .with(AttTeamBasePageQueryVo::setName, name)
                .with(AttTeamBasePageQueryVo::setOrderColumn, orderColumn)
                .with(AttTeamBasePageQueryVo::setAscOrDesc, ascOrDesc)
                .build());
    }

    @PostMapping("listWarehouses")
    @Override
    public CommonResult listWarehouses(String areaCode, String typeId, String name, String orderColumn, Boolean ascOrDesc) {
        return waterDisasterPreventionService.listWarehouses(Builder.of(AttWarehousePageQueryVo::new)
                .with(AttWarehousePageQueryVo::setAreaCode, areaCode)
                .with(AttWarehousePageQueryVo::setName, name)
                .with(AttWarehousePageQueryVo::setOrderColumn, orderColumn)
                .with(AttWarehousePageQueryVo::setAscOrDesc, ascOrDesc)
                .build());
    }

    @GetMapping("digitalShanXiOverview")
    @Override
    public CommonResult digitalShanXiOverview(String reservoirName) {
        return CommonResult.success(waterDisasterPreventionService.digitalShanXiOverview(reservoirName));
    }

    @GetMapping("testWarnMapPoint")
    public CommonResult testWarnMapPoint() {
        return CommonResult.success(waterDisasterPreventionService.testWarnMapPoint());
    }

}
