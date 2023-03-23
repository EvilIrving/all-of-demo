package com.ygkj.flood.feign;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xq
 * @Description
 * @Date 2021/4/15
 */
@FeignClient(contextId = "meteorological-visualization", name = "meteorological-visualization")
public interface MeteorologicalClient {

    @PostMapping("/weatherPredict/caculateRainfall")
    CommonResult<JSONObject> caculateRainfall(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude);

    @GetMapping("weatherPredict/rainfallPredictByIndex")
    CommonResult<JSONObject> rainfallPredictByIndex(@RequestParam("rowIndex") int rowIndex, @RequestParam("columnIndex") int columnIndex);
}
