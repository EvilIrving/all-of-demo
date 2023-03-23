package com.ygkj.water.controller;

import com.ygkj.soft.database.api.TableDivideApi;
import com.ygkj.soft.database.api.VO.TableDivideVO;
import com.ygkj.soft.database.api.model.BusDataResource;
import com.ygkj.water.client.SoftDataFeignClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huang.zh
 * @date 2020/9/15 14:18
 * @Description: 库表划分服务调用方控制层
 */
@RestController
@RequestMapping("/tableDivide")
public class TableDivideController implements TableDivideApi {

    @Autowired
    private SoftDataFeignClient softDataFeignClient;

    @PostMapping("/addResource")
    @Override
    public CommonResult addResource(BusDataResource busDataResource) {
        return softDataFeignClient.addResource(busDataResource);
    }

    @PostMapping("/divideTableResource")
    @Override
    public CommonResult divideTableResource(TableDivideVO tableDivideVO) {
        return softDataFeignClient.divideTableResource(tableDivideVO);
    }

    @GetMapping("/busDataResource")
    @Override
    public CommonResult busDataResource(BusDataResource busDataResource) {
        return softDataFeignClient.busDataResource(busDataResource);
    }

    @DeleteMapping("/busDataResource/{resourceId}")
    @Override
    public CommonResult deleteBusDataResource(@PathVariable String resourceId) {
        return softDataFeignClient.deleteBusDataResource(resourceId);
    }

    @PutMapping("/busDataResource")
    @Override
    public CommonResult updateBusDataResource(BusDataResource busDataResource) {
        return softDataFeignClient.updateBusDataResource(busDataResource);
    }
}
