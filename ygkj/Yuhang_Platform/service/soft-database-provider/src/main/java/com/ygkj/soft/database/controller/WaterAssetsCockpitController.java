package com.ygkj.soft.database.controller;

import com.alibaba.fastjson.JSON;
import com.ygkj.soft.database.service.WaterAssetsCockpitService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huang.zh
 * @date 2020/9/21 15:34
 * @Description: 水利资产大屏服务控制层
 */
@RestController
@RequestMapping("/WaterAssetsCockpit")
public class WaterAssetsCockpitController {

    @Autowired
    private WaterAssetsCockpitService waterAssetsCockpitService;

    @GetMapping("")
    public CommonResult queryData(){
        return CommonResult.success(JSON.parseObject(waterAssetsCockpitService.queryData()));
    }
}
