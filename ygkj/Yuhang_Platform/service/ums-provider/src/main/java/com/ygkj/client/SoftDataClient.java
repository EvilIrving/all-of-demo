package com.ygkj.client;

import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.model.BusDataTable;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Huang.zh
 * @date 2020/9/9 9:50
 * @Description: 通过open-feign调用数据中心服务封装
 */
@FeignClient(name = "soft-database")
public interface SoftDataClient {

    @GetMapping("/schema/busDataSource")
    CommonResult getBusDataSource(@RequestBody BusDataSource busDataSource);

    @PostMapping("/schema/addDataSource")
    CommonResult addDataSource(BusDataBaseAddVO dataBaseAddVO);

    @PostMapping("/schema/initDataSource")
    CommonResult initDataSource(String dataSourceID);

    @GetMapping("/schema/getDataSourceTables")
    CommonResult getDataSourceTables(BusDataTable dataTable);

    @GetMapping("/schema/getDataSourceColumn")
    CommonResult getDataSourceColumn(BusDataColumn column);

    @PostMapping("/schema/testDataSourceConnection")
    CommonResult testDataSourceConnection(BusDataBaseAddVO dataBaseAddVO);

    @PostMapping("/businessData/queryBusinessDataByPage")
    CommonResult queryBusinessDataByPage(BusinessDataQueryVO queryVO);

    @PostMapping("/businessData/insertSoftData")
    CommonResult insertSoftData(@RequestBody @Valid SoftDataAddVO softDataAddVO);

    @PostMapping("/businessData/querySoftData")
    CommonResult querySoftData(SoftDataQueryVO softDataQueryVO);

    @PostMapping("/businessData/updateSoftDataStatus")
    CommonResult updateSoftDataStatus(SoftDataUpdateVO softDataUpdateVO);
}
