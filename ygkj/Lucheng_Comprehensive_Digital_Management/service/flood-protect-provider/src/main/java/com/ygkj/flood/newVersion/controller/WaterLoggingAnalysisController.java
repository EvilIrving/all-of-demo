package com.ygkj.flood.newVersion.controller;

import com.ygkj.flood.WaterLoggingAnalysisControllerApi;
import com.ygkj.flood.model.AttFloodPoint;
import com.ygkj.flood.model.FloodPoint;
import com.ygkj.flood.service.WaterLoggingAnalysisService;
import com.ygkj.flood.vo.request.FloodPointReqVo;
import com.ygkj.flood.vo.response.ChartResVo;
import com.ygkj.flood.vo.response.FloodPointCurveResVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/waterLogging")
public class WaterLoggingAnalysisController implements WaterLoggingAnalysisControllerApi {

    @Autowired
    WaterLoggingAnalysisService waterLoggingAnalysisService;

    @GetMapping("floodPoint")
    @Override
    public CommonResult floodPoint(FloodPointReqVo reqVo) {
        return CommonResult.success(waterLoggingAnalysisService.listFloodPoint(reqVo));
    }

    @PostMapping("listFloodPoint")
    @Override
    public CommonResult listFloodPoint(@RequestBody FloodPointReqVo reqVo) {
        return CommonResult.success(waterLoggingAnalysisService.listFloodPoint(reqVo));
    }

    @GetMapping("pageFloodPoint")
    @Override
    public CommonResult pageFloodPoint(FloodPointReqVo reqVo) {
        return CommonResult.success(waterLoggingAnalysisService.pageFloodPoint(reqVo));
    }

    @GetMapping("stats4PageFloodPoint")
    @Override
    public CommonResult stats4PageFloodPoint(FloodPointReqVo reqVo) {
        return CommonResult.success(waterLoggingAnalysisService.stats4PageFloodPoint(reqVo));
    }

    @PostMapping("exportFloodPoint")
    @Override
    public void exportFloodPoint(FloodPointReqVo reqVo, HttpServletResponse response) {
        if (reqVo.getExportStrategy() == null) {
            reqVo.setExportStrategy(0);
        }
        // 禁止分页
        reqVo.noPage();
        List<AttFloodPoint> floodPointList = waterLoggingAnalysisService.listFloodPoint(reqVo);
        switch (reqVo.getExportStrategy()) {
            case 0: {
                ExportUtils.exportExcel(floodPointList, "积水",
                        new String[]{"积水点名称", "行政区划", "地址", "是否有积水", "积水水位"},
                        new String[]{"name", "areaName", "streetName", "ponding", "waterLevel"}, response);
            }
            break;
        }
    }

    @GetMapping("floodPointCWindows")
    @Override
    public CommonResult floodPointCWindows(@RequestParam(value = "dataTime", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime) {
        return CommonResult.success(this.waterLoggingAnalysisService.floodPointCWindows(dataTime));
    }

    @GetMapping("floodPointHeatMap")
    @Override
    public CommonResult floodPointHeatMap(@RequestParam(value = "dataTime", required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime) {
        return CommonResult.success(this.waterLoggingAnalysisService.floodPointHeatMap(dataTime));
    }

    @GetMapping("floodPointHistoricCurve")
    @Override
    public CommonResult floodPointHistoricCurve(@RequestParam("name") String name,
                                                @RequestParam("hours") Integer hours) {
        return CommonResult.success(waterLoggingAnalysisService.floodPointHistoricCurve(name, hours));
    }

    @GetMapping("floodPointCurveList")
    @Override
    public CommonResult floodPointCurveList(Integer limit) {
        return CommonResult.success(waterLoggingAnalysisService.floodPointCurveList(limit));
    }

    @GetMapping("floodPointForecastCurve")
    @Override
    public CommonResult floodPointForecastCurve(String name) {
        return CommonResult.success(waterLoggingAnalysisService.floodPointForecastCurve(name));
    }

    @GetMapping("floodPointHistoricForecastCurve")
    @Override
    public CommonResult floodPointHistoricForecastCurve(String name) {
        FloodPointCurveResVo result = new FloodPointCurveResVo();

        List<ChartResVo<Date, BigDecimal>> historicCurveTemp = waterLoggingAnalysisService.floodPointHistoricCurve(name, 24);
        List<ChartResVo<Date, BigDecimal>> forecastCurveTemp = waterLoggingAnalysisService.floodPointForecastCurve(name);

        // 这一步组合是为了方便前端叠加历史曲线和预测曲线
        List<ChartResVo<Date, BigDecimal>> historicCurve = new ArrayList<>(historicCurveTemp.size() + forecastCurveTemp.size());
        historicCurve.addAll(historicCurveTemp);
        for (ChartResVo<Date, BigDecimal> temp : forecastCurveTemp) {
            historicCurve.add(new ChartResVo<>(temp.getKey(), null));
        }
        List<ChartResVo<Date, BigDecimal>> forecastCurve = new ArrayList<>(historicCurveTemp.size() + forecastCurveTemp.size());
        for (ChartResVo<Date, BigDecimal> temp : historicCurveTemp) {
            forecastCurve.add(new ChartResVo<>(temp.getKey(), null));
        }
        forecastCurve.addAll(forecastCurveTemp);
        result.setHistoricCurve(historicCurve);
        result.setForecastCurve(forecastCurve);
        return CommonResult.success(result);
    }

    @GetMapping("waterLoggingHistoricForecastCurve")
    @Override
    public CommonResult waterLoggingHistoricForecastCurve(String name, Integer hours) {
        hours = hours == null ? 24 : hours;
        FloodPointCurveResVo result = new FloodPointCurveResVo();
        List<ChartResVo<Date, BigDecimal>> historicCurveTemp = waterLoggingAnalysisService.floodPointHistoricCurve(name, hours);
        List<ChartResVo<Date, BigDecimal>> forecastCurveTemp = waterLoggingAnalysisService.waterLoggingForecastCurve(name);

        // 这一步组合是为了方便前端叠加历史曲线和预测曲线
        List<ChartResVo<Date, BigDecimal>> historicCurve = new ArrayList<>(historicCurveTemp.size() + forecastCurveTemp.size());
        historicCurve.addAll(historicCurveTemp);
        for (ChartResVo<Date, BigDecimal> temp : forecastCurveTemp) {
            historicCurve.add(new ChartResVo<>(temp.getKey(), null));
        }
        List<ChartResVo<Date, BigDecimal>> forecastCurve = new ArrayList<>(historicCurveTemp.size() + forecastCurveTemp.size());
        for (ChartResVo<Date, BigDecimal> temp : historicCurveTemp) {
            forecastCurve.add(new ChartResVo<>(temp.getKey(), null));
        }
        forecastCurve.addAll(forecastCurveTemp);
        result.setHistoricCurve(historicCurve);
        result.setForecastCurve(forecastCurve);
        return CommonResult.success(result);
    }

    @GetMapping("floodPointStatistic")
    @Override
    public CommonResult floodPointStatistic(@RequestParam("type") int type) {
        return CommonResult.success(waterLoggingAnalysisService.floodPointStatistic(type));
    }

    @GetMapping("riverWaterLevel")
    @Override
    public CommonResult riverWaterLevel(String basin) {
        return CommonResult.success(waterLoggingAnalysisService.riverWaterLevel(basin));
    }

    @GetMapping("wagaList")
    @Override
    public CommonResult wagaList(String basin) {
        return CommonResult.success(waterLoggingAnalysisService.wagaList(basin));
    }

    @GetMapping("floodRisk")
    @Override
    public CommonResult floodRisk() {
        return CommonResult.success(waterLoggingAnalysisService.floodRisk());
    }

    @GetMapping("warnNum")
    @Override
    public CommonResult warnNum() {
        return CommonResult.success(waterLoggingAnalysisService.warnNum());
    }
}
