package com.ygkj.water.project.controller;


import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.StormSurgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("stormSurge")
@Api(tags = "风暴潮")
public class StormSurgeController {

    protected HttpServletResponse response;
    @Autowired
    private StormSurgeService stormSurgeService;


    /**
     * 获取风暴潮波高
     * @param tideLevelJsonArray	天文潮json  [{"tideLevel":"1.05",time:"2021-05-25 17:00:00"},{"tideLevel":"1.35",time:"2021-05-25 18:00:00"}]
     * @param windSpeedJsonArray	风速json  [{"windSpeed":"1.05",time:"2021-05-25 17:00:00"},{"windSpeed":"1.35",time:"2021-05-25 18:00:00"}]
     * @param fetchLength			风区长度
     * @param averageDepth			水深
     * @return
     */
    @PostMapping("/forecastWaveHeight")
    @ApiOperation(value = "获取风暴潮波高")
    public CommonResult forecastWaveHeight(@RequestParam(required = true) String tideLevelJsonArray,
                               @RequestParam(required = true) String windSpeedJsonArray,
                               @RequestParam(required = true) Double fetchLength,
                               @RequestParam(required = true) Double averageDepth){
        // 跨域
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Cache-Control","no-cache");
        try {
            Object result = stormSurgeService.forecastWaveHeight(tideLevelJsonArray, windSpeedJsonArray, fetchLength, averageDepth);
            return CommonResult.success(result);
        }catch (Exception e){
            return CommonResult.failed("参数错误，请检查参数格式或者值是否正确！");
        }

    }
}
