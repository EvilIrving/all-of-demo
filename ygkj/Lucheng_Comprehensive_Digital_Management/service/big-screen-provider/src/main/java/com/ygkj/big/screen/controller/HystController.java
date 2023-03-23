package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.HystControllerApi;
import com.ygkj.big.screen.service.HystService;
import com.ygkj.big.screen.vo.request.HystReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 水电站组件   宏泰数据库取实时数据
 *
 * @author lxl
 */
@RestController
@RequestMapping("/hystComponent")
public class HystController implements HystControllerApi {

    private HystService hystService;

    public HystController(@Autowired HystService hystService) {
        this.hystService = hystService;
    }

    @Override
    @GetMapping("/countByType")
    public CommonResult countByType(Byte type) {
        return CommonResult.success(hystService.countNumByType(type));
    }

    @Override
    @GetMapping("/rateOrderByMoon")
    public CommonResult rateOrderByMoon(Byte type, Integer moon, Integer year) {
        return CommonResult.success(hystService.rateOrderByMoon(type, moon, year));
    }

    @Override
    @GetMapping("/mainTotalNum")
    public CommonResult mainTotalNum() {
        return CommonResult.success(hystService.mainTotalNum());
    }

    @Override
    @PostMapping("/hystList")
    public CommonResult hystDetailListByCondition(HystReqVo reqVo) {
        return CommonResult.success(hystService.hystDetailListByCondition(reqVo));
    }

    @Override
    @GetMapping("/detail")
    public CommonResult hystDetailInfoByCode(String hystCode) {
        return CommonResult.success(hystService.hystDetailInfoByCode(hystCode));
    }

    @Override
    @PostMapping("/getFlowCurve")
    public CommonResult getFlowCurve(String hystCode, String startTime, String endTime) {
        return CommonResult.success(hystService.getFlowCurve(hystCode, startTime, endTime));
    }


    @Override
    @GetMapping("/dropPoint")
    public CommonResult dropPointHyst() {
        return CommonResult.success(hystService.dropPointHyst());
    }
}
