package com.ygkj.big.screen.feign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/20
 */
@FeignClient(contextId = "meteorological-visualization", name = "meteorological-visualization")
public interface MeteorologicVisualizationClient {

    @GetMapping("weatherPredict/weatherPredict/1")
    CommonResult<JSONArray> weatherPredict();

    @GetMapping("weatherPredict/weatherPredict/{areaName}")
    CommonResult weatherPredict(@PathVariable String areaName);

    @GetMapping("weatherPredict/weatherStatistic")
    CommonResult<List<LinkedHashMap>> weatherStatistic(@RequestParam("areaName") String areaName, @RequestParam("type") String type);

    @GetMapping("typhoons/currentTyphoon")
    CommonResult<List<LinkedHashMap>> currentTyphoon();

    @GetMapping("typhoons/newestTyphoon")
    CommonResult newestTyphoon();

    /**
     * @param code 台风编号
     * @param type 分析类型：1.路径相似path 2.季节相似season 3.生成地相似 born_land  可用逗号隔开
     * @return
     */
    @GetMapping("typhoons/analysis")
    CommonResult<LinkedHashMap> analysis(@RequestParam("code") String code, @RequestParam("type") String type);

    /**
     * 预报一个点位未来几小时的降水量
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    @PostMapping("/weatherPredict/caculateRainfall")
    CommonResult<LinkedHashMap> caculateRainfall(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude);


    @GetMapping("/weatherPredict/predictRainfallStatistic")
    CommonResult predictRainfallStatistic();

    @PostMapping("/weatherPredict/predictRainfallByAreaName")
    CommonResult predictRainfallByAreaName(String areaName);

    @PostMapping("/weatherPredict/rainfallMaxValuePredict")
    CommonResult rainfallMaxValuePredict(@RequestParam Integer hour);

    @PostMapping("/weatherPredict/rainfallPredictGrid")
    CommonResult rainfallPredictGrid(@RequestParam String type);

    @GetMapping("/weatherPredict/wholeWeather")
    CommonResult wholeWeather();
}
