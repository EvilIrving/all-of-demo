package com.ygkj.water.project.controller;

import com.ygkj.project.model.StWaterPlantR;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.WaterPlantService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pqf
 * @create 2023-02-17 10:56
 * @description
 */
@Api(tags = "农饮水")
@RestController
@RequestMapping("/waterPlant")
public class WaterPlantController {
    @Autowired
    private WaterPlantService waterPlantService;

    @PostMapping("/insertBatch")
    public CommonResult insertBatch(@RequestBody List<StWaterPlantR> list){
        try{
            waterPlantService.insertBatch(list);
            return CommonResult.success();
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }
}
