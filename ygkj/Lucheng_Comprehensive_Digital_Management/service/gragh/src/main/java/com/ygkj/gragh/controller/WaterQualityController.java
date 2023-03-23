package com.ygkj.gragh.controller;

import com.ygkj.gragh.WaterQualityControllerApi;
import com.ygkj.gragh.model.BswqRatio;
import com.ygkj.gragh.service.WaterQualityService;
import com.ygkj.gragh.vo.request.BswqReqVo;
import com.ygkj.gragh.vo.response.BswqResVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@RestController
@RequestMapping("waterQuality")
public class WaterQualityController implements WaterQualityControllerApi {

    @Autowired
    WaterQualityService waterQualityService;

    @GetMapping("list")
    @Override
    public CommonResult<List<BswqResVo>> list(BswqReqVo reqVo) {
        return CommonResult.success(waterQualityService.list(reqVo));
    }

    @GetMapping("bswqLevel")
    @Override
    public CommonResult<List<BswqResVo>> bswqLevel(@RequestParam("stationCode") String stationCode, @RequestParam("days") int days) {
        return CommonResult.success(waterQualityService.bswqLevel(stationCode, days));
    }

    @GetMapping("bswqRatio")
    @Override
    public CommonResult<List<BswqRatio>> bswqRatio(@RequestParam("type") int type, @RequestParam("months") int months) {
        return CommonResult.success(waterQualityService.bswqRatio(type, months));
    }

    @GetMapping("bswqStatistic")
    @Override
    public CommonResult<List<ChartResVo<String, Long>>> bswqStatistic(@RequestParam("type") int type) {
        return CommonResult.success(waterQualityService.bswqStatistic(type));
    }
}
