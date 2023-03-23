package com.ygkj.water.controller;

import com.ygkj.soft.database.api.DataSourceDivideApi;
import com.ygkj.soft.database.api.VO.BusDataBaseAddVO;
import com.ygkj.soft.database.api.VO.BusinessDataQueryVO;
import com.ygkj.soft.database.api.VO.DataSourceDivideVO;
import com.ygkj.water.client.SoftDataFeignClient;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.service.SoftDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huang.zh
 * @date 2020/9/16 17:05
 * @Description: 数源划分控制层
 */
@RestController
@RequestMapping("/dataSourceDivide")
public class DataSourceDivideController implements DataSourceDivideApi {

    @Autowired
    private SoftDataService softDataService;

    @Autowired
    private SoftDataFeignClient softDataFeignClient;


    @PostMapping("/data")
    @Override
    public CommonResult businessDataWithDivideType(BusinessDataQueryVO businessDataQueryVO) {
        return softDataService.getBussinessDataWithDepartment(businessDataQueryVO);
    }

    @PutMapping("")
    @Override
    public CommonResult divideBusinessData(DataSourceDivideVO divideVO) {
        return softDataFeignClient.divideBusinessData(divideVO);
    }
}
