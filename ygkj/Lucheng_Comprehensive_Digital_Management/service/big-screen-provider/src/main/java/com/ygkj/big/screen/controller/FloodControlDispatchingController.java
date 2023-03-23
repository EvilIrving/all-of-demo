package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.model.HeavyRainsArchive;
import com.ygkj.big.screen.service.FloodControlDispatchingService;
import com.ygkj.big.screen.service.HeavyRainsArchiveService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wgf
 * @date 2022/5/6 10:21
 * @Description: 戍浦江防洪调度大屏
 */
@RestController
@RequestMapping("floodControlDispatching")
public class FloodControlDispatchingController {

    @Autowired
    private FloodControlDispatchingService dispatchingService;

    @Autowired
    private HeavyRainsArchiveService heavyRainsArchiveService;

    @ApiOperation("戍浦江防洪调度-地图显示")
    @PostMapping("/mapPoints")
    public Object mapPoints(){
        return dispatchingService.mapPoints();
    }

    @ApiOperation("戍浦江防洪调度-工程目录")
    @PostMapping("/projectList")
    public Object projectList(String name){
        return CommonResult.success(dispatchingService.projectList(name));
    }

    @ApiOperation("戍浦江防洪调度-断面弹窗基本信息")
    @PostMapping("/dmInfo")
    public Object dmInfo(String id, String startTime, String endTime){
        return CommonResult.success(dispatchingService.dmInfo(id,startTime,endTime));
    }

    @ApiOperation(value = "实时降雨")
    @RequestMapping("/hourlyRainfall")
    public CommonResult hourlyRainfall(HeavyRainsArchive heavyRainsArchive) {
        Integer hours = heavyRainsArchive.getHours();
        if (hours != null){
            String endTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
            String startTime = LocalDateTime.now().minusHours(hours).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
            heavyRainsArchive.setEndTime(endTime);
            heavyRainsArchive.setStartTime(startTime);
        }
        return CommonResult.success(heavyRainsArchiveService.hourlyRainfallvalue(heavyRainsArchive));
    }

    @ApiOperation(value = "预测")
    @RequestMapping("/forecast")
    public CommonResult forecast(String index){
        return CommonResult.success(dispatchingService.forecast(index));
    }

    @ApiOperation(value = "预案")
    @PostMapping("/plan")
    public CommonResult plan(){
        return CommonResult.success(dispatchingService.plan());
    }

    @ApiOperation(value = "预案监管")
    @PostMapping("/planSupervision")
    public CommonResult planSupervision(){
        return CommonResult.success(dispatchingService.planSupervision());
    }

    @ApiOperation(value = "断面下拉选择")
    @PostMapping("/chooseSection")
    public CommonResult chooseSection(){
        return CommonResult.success(dispatchingService.chooseSection());
    }

    @ApiOperation(value = "新版预测 只有断面选择")
    @PostMapping("/forecastSection")
    public CommonResult forecastSection(String sectionIds){
        return CommonResult.success(dispatchingService.forecastSection(sectionIds));
    }

    @ApiOperation(value = "戍浦江防洪调度-断面弹窗水位过程线")
    @PostMapping("/dmWaterLevel")
    public CommonResult dmWaterLevel(String id){
        return CommonResult.success(dispatchingService.dmWaterLevel(id));
    }
}
