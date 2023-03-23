package com.ygkj.soft.database.api;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.api.model.BusDataColumn;
import com.ygkj.soft.database.api.model.BusDataSource;
import com.ygkj.soft.database.api.VO.BusDataBaseAddVO;
import com.ygkj.soft.database.api.VO.BusDataTableAddVo;
import com.ygkj.soft.database.api.VO.BusDataTablePageQueryVo;
import com.ygkj.soft.database.api.VO.BusDataTableStatusChangeVo;
import com.ygkj.soft.database.api.model.BusDataTable;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2020/9/7 17:27
 * @Description: 数据库元数据服务API抽象
 */
@Api(value = "SchemaApi",tags = "数据源元数据信息服务")
public interface SchemaApi {

    @ApiOperation(value = "查询数据源列表")
    CommonResult getBusDataSource(BusDataSource dataSource);

    @ApiOperation(value = "添加数据源")
    CommonResult addDataSource(BusDataBaseAddVO dataBaseAddVO);

    @ApiOperation(value = "测试数据源连接")
    CommonResult testDataSourceConnection(BusDataBaseAddVO dataBaseAddVO);

    @ApiOperation(value = "初始化数据源")
    CommonResult initDataSource(String dataSourceID);

    @ApiOperation(value = "获取数据源下的表")
    CommonResult getDataSourceTables(BusDataTable table);

    @ApiOperation(value = "获取表的字段")
    CommonResult getDataSourceColumn(BusDataColumn column);

    @ApiOperation(value = "分页查询指定条件下的表")
    CommonResult queryBusDataTableByPage(BusDataTablePageQueryVo vo);

    @ApiOperation(value = "新增表")
    CommonResult addBusDataTable(BusDataTableAddVo vo);
    @ApiOperation(value = "修改表的审核状态")
    CommonResult changeTableStatus(BusDataTableStatusChangeVo vo);
}
