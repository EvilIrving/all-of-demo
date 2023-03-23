package com.ygkj.water.client;

import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.BusDataResource;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huang.zh
 * @date 2020/9/15 9:56
 * @Description: 数据源服务open feign调用封装
 */
@FeignClient(contextId = "SoftData",name = "soft-database")
public interface SoftDataFeignClient {

    @PostMapping("/tableDivide/busDataResource")
    CommonResult addResource(BusDataResource busDataResource);

    @PostMapping("/tableDivide/divideTableResource")
    CommonResult divideTableResource(TableDivideVO tableDivideVO);

    @GetMapping("/tableDivide/busDataResource")
    CommonResult busDataResource(BusDataResource busDataResource);

    @DeleteMapping("/tableDivide/busDataResource")
    CommonResult deleteBusDataResource(@RequestParam("resourceId") String resourceId);

    @PutMapping("/tableDivide/busDataResource")
    CommonResult updateBusDataResource(BusDataResource busDataResource);

    @PutMapping("/dataSourceDivide")
    CommonResult divideBusinessData(DataSourceDivideVO divideVO);

    @GetMapping("/WaterAssetsCockpit")
    CommonResult queryData();

    @PostMapping("/schema/queryBusDataTableByPage")
    CommonResult queryBusDataTableByPage(BusDataTablePageQueryVo vo);
    @PostMapping("/schema/addBusDataTable")
    CommonResult addBusDataTable(BusDataTableAddVo vo);

    @PostMapping("/schema/changeTableStatus")
    CommonResult changeTableStatus(BusDataTableStatusChangeVo vo);
}
