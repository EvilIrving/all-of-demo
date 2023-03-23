package com.ygkj.visualization.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.visualization.WeatherPredictControllerApi;
import com.ygkj.visualization.service.WeatherService;
import com.ygkj.visualization.vo.request.RainfallPredictGridReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/weatherPredict")
public class WeatherPredictController implements WeatherPredictControllerApi {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/currentWeather")
    @Override
    public CommonResult currentWeather(String areaName) {
        try {
            return CommonResult.success(weatherService.currentWeather(areaName));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @GetMapping("/weatherPredict/{areaName}")
    @Override
    public CommonResult weatherPredict(@PathVariable(value = "areaName",required = false) String areaName) {
        try {
            return CommonResult.success(weatherService.weatherPredict(areaName));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @GetMapping("/weatherStatistic")
    @Override
    public CommonResult weatherStatistic(String areaName,String type) {
        try {
            return CommonResult.success(weatherService.weatherStatistic(areaName,type));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @PostMapping("/mapPointDetail")
    @Override
    public CommonResult mapPointDetail(String longitude, String latitude) {
        return CommonResult.success(weatherService.rainfallWaterFromSiChuang(longitude, latitude));
    }

    @PostMapping("/rainfallPredict")
    @Override
    public CommonResult rainfallPredict(Integer hour) {
        return CommonResult.success(weatherService.caculateAllRainStationRainfall(hour));
    }

    @PostMapping("/caculateRainfall")
    @Override
    public CommonResult caculateRainfall(String longitude, String latitude) {
        return CommonResult.success(weatherService.caculateRainfall(longitude, latitude));
    }

    @PostMapping("/rainAnalysis")
    @Override
    public CommonResult rainAnalysis(String year, String month) {
        return CommonResult.success(weatherService.rainAnalysis(year, month));
    }

    @GetMapping("/rainfallForecast")
    @Override
    public CommonResult rainfallPredict() {
        return CommonResult.success(weatherService.rainfallPredict());
    }

    @GetMapping("/situationAnalysis")
    @Override
    public CommonResult situationAnalysis() {
        return CommonResult.success(weatherService.situationAnalysis());
    }

    @GetMapping("surfaceRainfall")
    @Override
    public CommonResult surfaceRainfall() {
        return CommonResult.success(weatherService.surfaceRainfall());
    }

    @GetMapping("/predictRainfallStatistic")
    @Override
    public CommonResult predictRainfallStatistic() {
        return CommonResult.success(weatherService.predictRainfallStatistic());
    }

    @GetMapping("/updateMap")
    @Override
    public CommonResult updateRainfallPredictMap() {
        return CommonResult.success(weatherService.updateRainfallPredictMap());
    }

    @PostMapping("/predictRainfallByAreaName")
    @Override
    public CommonResult predictRainfallByAreaName(@RequestBody(required = false)String areaName) {
        return CommonResult.success(weatherService.predictRainfallByAreaName(areaName));
    }

    @PostMapping("/rainfallMaxValuePredict")
    @Override
    public CommonResult rainfallMaxValuePredict(Integer hour) {
        return CommonResult.success(weatherService.rainfallMaxValuePredict(hour));
    }

    @PostMapping("/rainfallPredictGrid")
    @Override
    public CommonResult rainfallPredictGrid(String type) {
        return CommonResult.success(weatherService.rainfallPredictGrid(type));
    }

    @PostMapping("/exportRainfallPredictGrid")
    @Override
    public void exportRainfallPredictGrid(@RequestBody RainfallPredictGridReqVo reqVo, HttpServletResponse response) {
        weatherService.exportRainfallPredictGrid(reqVo.getType(),response);
    }

    @GetMapping("/wholeWeather")
    @Override
    public CommonResult wholeWeather() {
        return CommonResult.success(weatherService.wholeWeather());
    }

    @GetMapping("/rainfallPredictByIndex")
    @Override
    public CommonResult<JSONObject> rainfallPredictByIndex(int rowIndex, int columnIndex) {
        return CommonResult.success(weatherService.rainfallPredictByIndex(rowIndex,columnIndex));
    }


    @Override
    @GetMapping("/listJsonForWeather")
    public CommonResult listJsonForWeather(String type) {
        return CommonResult.success(weatherService.listJsonForWeather(type));
    }


    @Override
    @GetMapping("/getRainFall")
    public CommonResult rainFallPredictForWz() {
        return CommonResult.success(weatherService.rainFallPredictForWz());
    }

    @Override
    @GetMapping("/rainFallGrid")
    public CommonResult rainFallGridForWz(Integer hour) {
        return CommonResult.success(weatherService.rainFallGridForWz(hour));
    }
}
