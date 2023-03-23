package com.ygkj.digitization.feign;

import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.vo.request.*;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
@FeignClient(contextId = "graph", name = "graph")
public interface GraghClient {

    @PostMapping("waterAndRain/waterStations")
    CommonResult<List<AttStBase>> waterStations(@SpringQueryMap SynWaterRequestVo synWaterRequestVo);

    @PostMapping("waterAndRain/waterStationsBody")
    CommonResult<List<AttStBase>> waterStationsBody(@RequestBody SynWaterRequestVo synWaterRequestVo);

    @GetMapping("waterRainStatistic/countStTypeNumByAdcdBasin")
    CommonResult<List<LinkedHashMap>> countStTypeNumByAdcdBasin(@RequestParam(value = "adcd", required = false) String adcd,
                                                                @RequestParam(value = "basin", required = false) String basin);

    @PostMapping("/waterAndRain/rainfallStatistic")
    CommonResult rainfallStatistic(@RequestParam(required = false) String areaName, @RequestParam(required = false) String endTime);

    @GetMapping("/waterAndRain/pastHoursRainfall")
    CommonResult pastHoursRainfall();

    /**
     * 水雨工情，历史监测值查询
     *
     * @param reqVo
     * @return
     */
    @PostMapping("waterAndRain/multiWaterRains")
    CommonResult<List<LinkedHashMap>> multiWaterRains(@RequestBody MultiWaterRainReqVo reqVo);

    @PostMapping("/waterAndRain/grandTotalMaxRainfallStatistic")
    CommonResult grandTotalMaxRainfallStatistic(@RequestParam(required = false) String endTime, @RequestParam(required = false) Integer hour);

    @PostMapping("/waterAndRain/grandTotalRainfallGrid")
    CommonResult grandTotalRainfallGrid(@SpringQueryMap SynRainRequestVo requestVo,
                                        @RequestParam(required = false) String endTime,
                                        @RequestParam(required = false) String type,
                                        @RequestParam(required = false) String surfaceType);

    @PostMapping("/waterAndRain/grandTotalReservoirRainfall")
    CommonResult grandTotalReservoirRainfall(@RequestParam(required = false) String endTime, @RequestParam(required = false) Integer hour);

    /**
     * 河网查询
     *
     * @param reqVo
     * @return
     */
    @PostMapping("waterAndRain/listRivernet")
    CommonResult<List<LinkedHashMap>> listRivernet(@SpringQueryMap RivernetWaterLevelReqVo reqVo);

    /**
     * 河网复盘统计
     *
     * @return
     */
    @GetMapping("waterAndRain/rivernetReplayStats")
    CommonResult rivernetReplayStats(@SpringQueryMap RivernetReplayReqVo reqVo);

    @GetMapping("waterAndRain/tideReplayStats")
    CommonResult<LinkedHashMap> tideReplayStats(@SpringQueryMap TideReplayReqVo reqVo);
}
