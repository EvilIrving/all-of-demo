package com.ygkj.visualization.controller;

import com.ygkj.visualization.CollectPointControllerApi;
import com.ygkj.visualization.service.CollectPointService;
import com.ygkj.visualization.vo.request.CollectPointAddReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xq
 * 
 * @Date 2021/2/26
 */
@RestController
@RequestMapping("/collectPoint")
public class CollectPointController implements CollectPointControllerApi {

    @Autowired
    CollectPointService collectPointService;

    @GetMapping("getAllPoint")
    @Override
    public CommonResult getAllPoint(String name) {
        return CommonResult.success(collectPointService.getAllPoint(name));
    }

    @PostMapping("addPoint")
    @Override
    public CommonResult addPoint(CollectPointAddReqVo collectPoint) {
        return CommonResult.success(collectPointService.addPoint(collectPoint));
    }

    @PostMapping("deletePoint")
    @Override
    public CommonResult deletePoint(String id) {
        return CommonResult.success(collectPointService.deletePoint(id));
    }
}
