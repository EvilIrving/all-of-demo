package com.ygkj.soft.database.controller;

import com.ygkj.soft.database.api.VO.DataSourceDivideVO;
import com.ygkj.soft.database.service.BusinessDataService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huang.zh
 * @date 2020/9/16 16:03
 * @Description: 数源划分服务控制层
 */
@RestController
@RequestMapping("/dataSourceDivide")
public class DataSourceDivideController {

    @Autowired
    private BusinessDataService businessDataService;

    @PutMapping("")
    public CommonResult divideBusinessData(@RequestBody DataSourceDivideVO divideVO){
        try {
            businessDataService.divideBusinessData(divideVO);
            return CommonResult.success("");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed();
        }
    }
}
