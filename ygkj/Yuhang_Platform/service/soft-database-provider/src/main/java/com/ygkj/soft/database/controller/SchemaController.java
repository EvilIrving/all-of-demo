package com.ygkj.soft.database.controller;

import com.ygkj.soft.database.api.SchemaApi;
import com.ygkj.soft.database.api.VO.BusDataBaseAddVO;
import com.ygkj.soft.database.api.VO.BusDataTableAddVo;
import com.ygkj.soft.database.api.VO.BusDataTablePageQueryVo;
import com.ygkj.soft.database.api.VO.BusDataTableStatusChangeVo;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.model.BusDataTable;
import com.ygkj.soft.database.service.SchemaService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @author Huang.zh
 * @date 2020/9/7 17:30
 * @Description: 数据源元数据服务
 */
@RestController
@RequestMapping("/schema")
public class SchemaController implements SchemaApi {

    @Autowired
    private SchemaService schemaService;

    @GetMapping("/busDataSource")
    @Override
    public CommonResult getBusDataSource(@RequestBody BusDataSource dataSource) {
        return CommonResult.success(schemaService.getBusDataSource(dataSource));
    }

    @PostMapping("/addDataSource")
    @Override
    public CommonResult addDataSource(@RequestBody BusDataBaseAddVO dataBaseAddVO) {
        try {
            schemaService.addDataSource(dataBaseAddVO);
            return CommonResult.success("");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }
    @PostMapping("/testDataSourceConnection")
    @Override
    public CommonResult testDataSourceConnection( @RequestBody BusDataBaseAddVO dataBaseAddVO) {
        try {
            if (schemaService.testDataSourceConnection(dataBaseAddVO)){
                return CommonResult.success("","连接成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CommonResult.failed("连接失败！");
    }

    @PostMapping("/initDataSource")
    @Override
    public CommonResult initDataSource(@RequestBody String dataSourceID) {
        try {
            if (schemaService.initDataSource(dataSourceID)){
                return CommonResult.success("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed("初始化失败！请联系管理员");
    }

    @GetMapping("/getDataSourceTables")
    @Override
    public CommonResult getDataSourceTables(@RequestBody BusDataTable dataTable) {
        return CommonResult.success(schemaService.getBusDataTable(dataTable));
    }
    @GetMapping("/getDataSourceColumn")
    @Override
    public CommonResult getDataSourceColumn(@RequestBody BusDataColumn column) {
        return CommonResult.success(schemaService.getBusDataColumn(column));
    }

    @PostMapping("/queryBusDataTableByPage")
    @Override
    public CommonResult queryBusDataTableByPage(@RequestBody BusDataTablePageQueryVo vo) {
        return CommonResult.success(schemaService.queryBusDataTableByPage(vo));
    }

    @PostMapping("/addBusDataTable")
    @Override
    public CommonResult addBusDataTable(@RequestBody BusDataTableAddVo vo) {
        try {
            schemaService.addBusDataTable(vo);
            return CommonResult.success("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return CommonResult.failed("操作失败");
        }
    }
    @PostMapping("/changeTableStatus")
    @Override
    public CommonResult changeTableStatus(@RequestBody BusDataTableStatusChangeVo vo) {
        try {
            schemaService.changeTableStatus(vo);
            return CommonResult.success("操作成功");
        }catch (Exception e){
            e.printStackTrace();

            return CommonResult.failed("操作失败");
        }
    }


}
