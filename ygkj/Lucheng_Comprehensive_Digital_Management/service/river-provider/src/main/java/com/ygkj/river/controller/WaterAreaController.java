package com.ygkj.river.controller;

import com.ygkj.river.WaterAreaControllerApi;
import com.ygkj.river.service.AttWaterAreaService;
import com.ygkj.river.vo.request.AttWaterAreaReqVo;
import com.ygkj.river.vo.response.AttWaterAreaRespVo;
import com.ygkj.river.vo.response.HistogramWaterArea;
import com.ygkj.river.vo.response.OverallStatisticsWaterArea;
import com.ygkj.river.vo.response.PositionStatisticsWaterArea;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author hucong
 * @description 水域面积controller
 * @date 2021-08-12
 */
@RestController
@RequestMapping(value = "/waterAreaContr")
public class WaterAreaController implements WaterAreaControllerApi {

    @Autowired
    private AttWaterAreaService attWaterAreaService;

    @GetMapping("overallStatisticsWaterArea")
    @Override
    public CommonResult<OverallStatisticsWaterArea> overallStatisticsWaterArea(Integer year) {
        return CommonResult.success(attWaterAreaService.overallStatisticsWaterArea(year));
    }

    @GetMapping("histogramWaterArea")
    @Override
    public CommonResult<Map<Integer, List<HistogramWaterArea>>> histogramWaterArea() {
        return CommonResult.success(attWaterAreaService.histogramWaterArea());
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttWaterAreaRespVo>> loadPage(AttWaterAreaReqVo vo) {
        return CommonResult.success(attWaterAreaService.loadPage(vo));
    }

    @GetMapping("cPositionStatisticsWaterArea")
    @Override
    public CommonResult<PositionStatisticsWaterArea> cPositionStatisticsWaterArea(Integer year) {
        return CommonResult.success(attWaterAreaService.cPositionStatisticsWaterArea(year));
    }

    @GetMapping("waterAreaEachAdministrative")
    @Override
    public CommonResult<List<AttWaterAreaRespVo>> waterAreaEachAdministrative(Integer year) {
        return CommonResult.success(attWaterAreaService.waterAreaEachAdministrative(year));
    }
}
