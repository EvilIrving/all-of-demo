package com.ygkj.digitization.controller;

import com.ygkj.digitization.service.RainfallMigrateService;
import com.ygkj.digitization.RainfallMigrateControllerApi;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author xq
 * 
 * @Date 2021/2/6
 */
@Deprecated
@RestController("rainfallMigrate")
public class RainfallMigrateController implements RainfallMigrateControllerApi {

    @Autowired
    RainfallMigrateService rainfallMigrateService;

    @Deprecated
    @GetMapping("getByHour")
    @Override
    public CommonResult getByHour(@RequestParam("hourDate")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date hourDate) {
        return CommonResult.success(rainfallMigrateService.getByHour(hourDate));
    }
}
