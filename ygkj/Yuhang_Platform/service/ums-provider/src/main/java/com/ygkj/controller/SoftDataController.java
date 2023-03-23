package com.ygkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.client.SoftDataClient;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.model.BusDataTable;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ygkj.com.assistant.pager.Pager;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/9 10:03
 * @Description: 数据中心服务控制层
 */
@RestController
@RequestMapping("/softData")
public class SoftDataController {

    @Autowired
    private SoftDataClient softDataClient;

    @GetMapping("/busDataSource")
    public CommonResult getBusDataSource(@RequestBody BusDataSource busDataSource){
        return softDataClient.getBusDataSource(busDataSource);
    }

    @PostMapping("/addDataSource")
    public CommonResult addDataSource(@RequestBody BusDataBaseAddVO dataBaseAddVO) {
        return softDataClient.addDataSource(dataBaseAddVO);
    }

    @PostMapping("/initDataSource")
    public CommonResult initDataSource(@RequestBody String dataSourceID){
        return softDataClient.initDataSource(dataSourceID);
    }

    @GetMapping("/getDataSourceTables")
    public CommonResult getDataSourceTables(@RequestBody BusDataTable dataTable) {
        return softDataClient.getDataSourceTables(dataTable);
    }

    @GetMapping("/getDataSourceColumn")
    public CommonResult getDataSourceColumn(@RequestBody BusDataColumn column){
        return softDataClient.getDataSourceColumn(column);
    }

    @PostMapping("/testDataSourceConnection")
    public CommonResult testDataSourceConnection(@RequestBody BusDataBaseAddVO dataBaseAddVO){
        return softDataClient.testDataSourceConnection(dataBaseAddVO);
    }

    @PostMapping("/queryBusinessDataByPage")
    public CommonResult queryBusinessDataByPage(@RequestBody BusinessDataQueryVO queryVO){
        return softDataClient.queryBusinessDataByPage(queryVO);
    }

    @PostMapping("/insertSoftData")
    public CommonResult insertSoftData(@RequestBody SoftDataAddVO softDataAddVO){
        return softDataClient.insertSoftData(softDataAddVO);
    }

    @PostMapping("/querySoftData")
    public CommonResult querySoftData(@RequestBody SoftDataQueryVO softDataQueryVO){
        return softDataClient.querySoftData(softDataQueryVO);
    }


    @PostMapping("/updateSoftDataStatus")
    public CommonResult updateSoftDataStatus(@RequestBody SoftDataUpdateVO softDataUpdateVO) {
        return softDataClient.updateSoftDataStatus(softDataUpdateVO);
    }
}
