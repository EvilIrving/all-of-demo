package com.ygkj.digitization.controller;

import com.ygkj.digitization.HydroFeatureControllerApi;
import com.ygkj.digitization.service.HydroFeatureService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/9/7
 */
@RestController
@RequestMapping("hydroFeature")
public class HydroFeatureController implements HydroFeatureControllerApi {

    @Autowired
    HydroFeatureService hydroFeatureService;

    @GetMapping("listFeatureTable")
    @Override
    public CommonResult listFeatureTable(String tableNameKey) {
        return CommonResult.success(hydroFeatureService.listFeatureTable(tableNameKey));
    }

    @GetMapping("loadFeatureValue")
    @Override
    public CommonResult loadFeatureValue(String tableName) {
        return CommonResult.success(hydroFeatureService.loadFeatureValue(tableName));
    }
}
