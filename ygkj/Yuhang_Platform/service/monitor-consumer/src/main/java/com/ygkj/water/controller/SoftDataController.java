package com.ygkj.water.controller;

import com.ygkj.soft.database.api.SchemaApi;
import com.ygkj.soft.database.api.VO.BusDataBaseAddVO;
import com.ygkj.soft.database.api.VO.BusDataTableAddVo;
import com.ygkj.soft.database.api.VO.BusDataTablePageQueryVo;
import com.ygkj.soft.database.api.VO.BusDataTableStatusChangeVo;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.model.BusDataTable;
import com.ygkj.water.client.SoftDataClient;
import com.ygkj.water.client.SoftDataFeignClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Huang.zh
 * @date 2020/9/9 10:18
 * @Description:
 */
@RestController
@RequestMapping("/softData")
public class SoftDataController implements SchemaApi {

    @Autowired
    private SoftDataClient softDataClient;

    @Autowired
    private SoftDataFeignClient softDataFeignClient;

    @Override
    @GetMapping("/busDataSource")
    public CommonResult getBusDataSource(BusDataSource busDataSource){
        return softDataClient.getBusDataSource(busDataSource);
    }

    @PostMapping("/addDataSource")
    @Override
    public CommonResult addDataSource(BusDataBaseAddVO dataBaseAddVO) {
        return softDataClient.addDataSource(dataBaseAddVO);
    }

    @PostMapping("/testDataSourceConnection")
    @Override
    public CommonResult testDataSourceConnection(BusDataBaseAddVO dataBaseAddVO) {
        return softDataClient.testDataSourceConnection(dataBaseAddVO);
    }

    @PostMapping("/initDataSource")
    @Override
    public CommonResult initDataSource(String dataSourceID) {
        return softDataClient.initDataSource(dataSourceID);
    }

    @GetMapping("/getDataSourceTables")
    @Override
    public CommonResult getDataSourceTables(BusDataTable table) {
        return softDataClient.getDataSourceTables(table);
    }

    @GetMapping("/getDataSourceColumn")
    @Override
    public CommonResult getDataSourceColumn(BusDataColumn column) {
        return softDataClient.getDataSourceColumn(column);
    }

    @PostMapping("/queryBusDataTableByPage")
    @Override
    public CommonResult queryBusDataTableByPage(BusDataTablePageQueryVo vo) {
        return softDataFeignClient.queryBusDataTableByPage(vo);
    }
    @PostMapping("/addBusDataTable")
    @Override
    public CommonResult addBusDataTable(@Valid BusDataTableAddVo vo) {
        return softDataFeignClient.addBusDataTable(vo);
    }
    @PostMapping("/changeTableStatus")
    @Override
    public CommonResult changeTableStatus(BusDataTableStatusChangeVo vo) {
        return softDataFeignClient.changeTableStatus(vo);
    }
}
