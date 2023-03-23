package com.ygkj.water.client;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.api.VO.BusDataBaseAddVO;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.model.BusDataTable;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/9 10:08
 * @Description:
 */
@FeignClient(contextId = "softData",name = "authentication-system")
public interface SoftDataClient {
    @GetMapping("/softData/busDataSource")
    CommonResult getBusDataSource(BusDataSource busDataSource);

    @PostMapping("/softData/addDataSource")
    CommonResult addDataSource(BusDataBaseAddVO dataBaseAddVO);

    @PostMapping("/softData/initDataSource")
    CommonResult initDataSource(String dataSourceID);

    @GetMapping("/softData/getDataSourceTables")
    CommonResult getDataSourceTables(BusDataTable dataTable);

    @GetMapping("/softData/getDataSourceColumn")
    CommonResult getDataSourceColumn(BusDataColumn column);

    @PostMapping("/softData/testDataSourceConnection")
    CommonResult testDataSourceConnection(BusDataBaseAddVO dataBaseAddVO);
}
