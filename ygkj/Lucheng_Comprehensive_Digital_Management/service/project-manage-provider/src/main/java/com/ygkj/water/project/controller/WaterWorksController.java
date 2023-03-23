package com.ygkj.water.project.controller;

import com.ygkj.project.WaterWorksControllerApi;
import com.ygkj.project.vo.request.WaterWorksReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.WaterWorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/8/16
 */
@RestController
@RequestMapping("waterWorks")
public class WaterWorksController implements WaterWorksControllerApi {

    @Autowired
    WaterWorksService waterWorksService;

    @GetMapping("listWaterWorks")
    @Override
    public CommonResult listWaterWorks(WaterWorksReqVo reqVo) {
        reqVo.setColumnNames(null);// 避免注入
        return CommonResult.success(waterWorksService.listWaterWorks(reqVo));
    }

    @GetMapping("stats4listWaterWorks")
    @Override
    public CommonResult stats4listWaterWorks(WaterWorksReqVo reqVo) {
        return CommonResult.success(waterWorksService.stats4listWaterWorks(reqVo));
    }

    @GetMapping("stats4listWaterWorksState")
    @Override
    public CommonResult stats4listWaterWorksState(WaterWorksReqVo reqVo) {
        return CommonResult.success(waterWorksService.stats4listWaterWorksState(reqVo));
    }

    @GetMapping("waterWorksStats")
    @Override
    public CommonResult<Object> waterWorksStats(@RequestParam("type") int type) {
        return CommonResult.success(waterWorksService.waterWorksStats(type));
    }

    @GetMapping("waterWorksStateCurve")
    @Override
    public CommonResult waterWorksStateCurve(@RequestParam("type") int type,
                                             @RequestParam("days") int days,
                                             @RequestParam("cwsCode") String cwsCode) {
        return CommonResult.success(waterWorksService.waterWorksStateCurve(type,days,cwsCode));
    }
}
