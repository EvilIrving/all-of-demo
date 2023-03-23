package com.ygkj.digitization.controller;

import com.ygkj.digitization.WaterStatusReportControllerApi;
import com.ygkj.digitization.mapper.ProjectMapper;
import com.ygkj.digitization.service.WaterStatusReportService;
import com.ygkj.digitization.vo.request.MrwlCurveReqVo;
import com.ygkj.digitization.vo.request.MrwlReqVo;
import com.ygkj.digitization.vo.request.WaterStatusReportReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
@RestController
@RequestMapping("waterStatusReport")
public class WaterStatusReportController implements WaterStatusReportControllerApi {

    @Autowired
    WaterStatusReportService waterStatusReportService;

    @GetMapping("rivernetWaterLevel")
    @Override
    public CommonResult rivernetWaterLevel(WaterStatusReportReqVo reqVo) {
        return waterStatusReportService.rivernetWaterLevel(reqVo.getDataTime(), reqVo.getName());
    }

    @GetMapping("listMrwlZqCurveConfig")
    @Override
    public CommonResult listMrwlZqCurveConfig() {
        return CommonResult.success(waterStatusReportService.listMrwlZqCurveConfig());
    }

    @GetMapping("mrwlCurve")
    @Override
    public CommonResult mrwlCurve(MrwlCurveReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.mrwlCurve(reqVo));
    }

    @PostMapping("mountainRiverWaterLevel")
    @Override
    public CommonResult mountainRiverWaterLevel(@RequestBody MrwlReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.mountainRiverWaterLevel(reqVo));
    }

    @GetMapping("riverWaterStats")
    @Override
    public CommonResult riverWaterStats(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.riverWaterStats(reqVo.getStart(), reqVo.getEnd(), reqVo.getName()));
    }

    @GetMapping("riverDayAvgWater")
    @Override
    public CommonResult riverDayAvgWater(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.riverDayAvgWater(reqVo.getStart(), reqVo.getEnd(), reqVo.getName()));
    }

    @GetMapping("riverOverWarn")
    @Override
    public CommonResult riverOverWarn(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.riverOverWarn(reqVo.getStart(), reqVo.getEnd(), reqVo.getName()));
    }

    @GetMapping("tideSubPerHour")
    @Override
    public CommonResult tideSubPerHour(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.tideSubPerHour(reqVo.getStart(), reqVo.getEnd(), reqVo.getName()));
    }

    @GetMapping("highTideSub")
    @Override
    public CommonResult highTideSub(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.highTideSub(reqVo.getStart(), reqVo.getEnd(), reqVo.getName()));
    }

    @GetMapping("rsvrStorageStats")
    @Override
    public CommonResult rsvrStorageStats(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.rsvrStorageStats(reqVo.getDataTime()));
    }

    @GetMapping("rsvrWaterStats")
    @Override
    public CommonResult rsvrWaterStats(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.rsvrWaterStats(reqVo.getStart(),reqVo.getEnd()));
    }

    @GetMapping("rsvrWaterTimeInterval")
    @Override
    public CommonResult rsvrWaterTimeInterval(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.rsvrWaterTimeInterval(reqVo.getStart(), reqVo.getEnd(), reqVo.getName()));
    }

    @GetMapping("rsvrWaterOverLimit")
    @Override
    public CommonResult rsvrWaterOverLimit(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.rsvrWaterOverLimit(reqVo.getStart(), reqVo.getEnd(), reqVo.getName()));
    }

    @GetMapping("rsvrWaterRainInfo")
    @Override
    public CommonResult rsvrWaterRainInfo(WaterStatusReportReqVo reqVo) {
        return CommonResult.success(waterStatusReportService.rsvrWaterRainInfo(reqVo.getStart(), reqVo.getEnd(), reqVo.getResCode()));
    }

    @Resource
    ProjectMapper projectMapper;

    @GetMapping("rsvrList")
    @Override
    public CommonResult rsvrList(@RequestParam("engScalType")String engScalType) {
        if ("大中型".equals(engScalType)){
            return CommonResult.success(projectMapper.selectLargeMidRsvr());
        }else{
            return CommonResult.success(Collections.emptyList());
        }
    }

    @PostMapping("rivernetWaterLevelExport")
    @Override
    public void rivernetWaterLevelExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.rivernetWaterLevelExport(reqVo,response);
    }

    @PostMapping("riverWaterStatsExport")
    @Override
    public void riverWaterStatsExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.riverWaterStatsExport(reqVo,response);
    }

    @PostMapping("riverDayAvgWaterExport")
    @Override
    public void riverDayAvgWaterExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.riverDayAvgWaterExport(reqVo,response);
    }

    @PostMapping("riverOverWarnExport")
    @Override
    public void riverOverWarnExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.riverOverWarnExport(reqVo,response);
    }

    @PostMapping("tideSubPerHourExport")
    @Override
    public void tideSubPerHourExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.tideSubPerHourExport(reqVo,response);
    }

    @PostMapping("highTideSubExport")
    @Override
    public void highTideSubExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.highTideSubExport(reqVo,response);
    }

    @PostMapping("rsvrStorageStatsExport")
    @Override
    public void rsvrStorageStatsExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.rsvrStorageStatsExport(reqVo,response);
    }

    @PostMapping("rsvrWaterStatsExport")
    @Override
    public void rsvrWaterStatsExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.rsvrWaterStatsExport(reqVo,response);
    }

    @PostMapping("rsvrWaterTimeIntervalExport")
    @Override
    public void rsvrWaterTimeIntervalExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.rsvrWaterTimeIntervalExport(reqVo,response);
    }

    @PostMapping("rsvrWaterOverLimitExport")
    @Override
    public void rsvrWaterOverLimitExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.rsvrWaterOverLimitExport(reqVo,response);
    }

    @PostMapping("rsvrWaterRainInfoExport")
    @Override
    public void rsvrWaterRainInfoExport(@RequestBody WaterStatusReportReqVo reqVo, HttpServletResponse response) {
        waterStatusReportService.rsvrWaterRainInfoExport(reqVo,response);
    }
}
