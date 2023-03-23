package com.ygkj.digitization.controller;

import com.ygkj.digitization.RainfallReportControllerApi;
import com.ygkj.digitization.service.WaterReportService;
import com.ygkj.digitization.vo.request.StationRequestVo;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ygkj.com.util.DateUtil;

import java.text.ParseException;

/**
 * 雨量报表controller
 */

@RestController
@RequestMapping("rainfallReport")
public class RainfallReportController implements RainfallReportControllerApi {


    @Autowired
    private WaterReportService waterReportService;

    @PostMapping("partitionRainfall")
    @Override
    public CommonResult partitionRainfall(StationRequestVo queryVo) throws ParseException {
        if (StringUtils.isEmpty(queryVo.getStartTime()) || StringUtils.isEmpty(queryVo.getEndTime())) {
            return CommonResult.failed("开始时间或结束时间为空!!!");
        }
        if (DateUtil.parseDate("yyyy-MM-dd HH",queryVo.getStartTime()).getTime() <
                DateUtil.parseDate("yyyy-MM-dd HH","2020-11-30 00").getTime()) {
            return CommonResult.failed("开始时间不能小于2020-11-30 00");
        }
        return CommonResult.success(waterReportService.partitionRainfall(queryVo));
    }

    @PostMapping("periodExtremum")
    @Override
    public CommonResult periodExtremumRain(StationRequestVo requestVo) {
        if (StringUtils.isEmpty(requestVo.getStartTime()) || StringUtils.isEmpty(requestVo.getEndTime())) {
            return CommonResult.failed("开始时间或结束时间为空!!!");
        }
        if (DateUtil.parseDate("yyyy-MM-dd HH",requestVo.getStartTime()).getTime() <
                DateUtil.parseDate("yyyy-MM-dd HH","2020-11-30 00").getTime()) {
            return CommonResult.failed("开始时间不能小于2020-11-30 00");
        }
        return CommonResult.success(waterReportService.periodExtremumRain(requestVo));
    }

    @PostMapping("dailyRainfall")
    @Override
    public CommonResult dailyRainfall(StationRequestVo requestVo) {
        if (StringUtils.isEmpty(requestVo.getStartTime()) || StringUtils.isEmpty(requestVo.getEndTime())) {
            return CommonResult.failed("开始时间或结束时间为空!!!");
        }
        Integer dayBetween = DateUtils.getDays(requestVo.getStartTime(), requestVo.getEndTime());
        if ( dayBetween > 12 ) {
            return CommonResult.failed("筛选不超过12天，请重新选择");
        }
        return CommonResult.success(waterReportService.dailyRainfall(requestVo));
    }

    @PostMapping("rainstormWarning")
    @Override
    public CommonResult rainstormWarning(StationRequestVo requestVo) {
        if (StringUtils.isEmpty(requestVo.getStartTime()) || StringUtils.isEmpty(requestVo.getEndTime())) {
            return CommonResult.failed("开始时间或结束时间为空!!!");
        }
        return CommonResult.success(waterReportService.rainstormWarning(requestVo));
    }


    @PostMapping("/rainstormWarnForOne")
    @Override
    public CommonResult rainstormWarnForOne(@RequestBody StationRequestVo requestVo) {
        if (StringUtils.isEmpty(requestVo.getStartTime()) || StringUtils.isEmpty(requestVo.getEndTime())) {
            return CommonResult.failed("开始时间或结束时间为空!!!");
        }
        return CommonResult.success(waterReportService.rainstormWarning(requestVo));
    }

    @Override
    @PostMapping("/rainstormWarnNumForOne")
    public CommonResult rainstormWarnNumListForOne(@RequestBody StationRequestVo requestVo){
        if (StringUtils.isEmpty(requestVo.getStartTime()) || StringUtils.isEmpty(requestVo.getEndTime())) {
            return CommonResult.failed("开始时间或结束时间为空!!!");
        }
        return CommonResult.success(waterReportService.rainstormWarnForOne(requestVo));
    }



}