package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.*;
import com.ygkj.data.management.vo.request.*;
import com.ygkj.entity.PageEntity;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(tags = "元数据服务", value = "元数据服务")
public interface SchemaControllerApi {

    @ApiOperation("查询某个数据清单下的表")
    CommonResult tables(SchemaTablePageQueryVo queryVo);

    @ApiOperation("查询某张表的字段")
    CommonResult columns(TableColumnsPageQueryVo queryVo);

    @ApiOperation("数据维护列表")
    CommonResult records(TableRecordPageQueryVo queryVo);

    @ApiOperation("单条数据详情")
    CommonResult recordDetail(TableRecordPageQueryVo queryVo);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "schema", value = "数据类型：基础数据/业务数据"),
            @ApiImplicitParam(name = "tableName", value = "表名"),
            @ApiImplicitParam(name = "primary", value = "主键的值，即第一个字段的值"),
    })
    @ApiOperation("单条数据的责任人列表")
    CommonResult responsibilities(String schema, String tableName, String primary);

    @ApiOperation("为一条数据指定责任人")
    CommonResult addResponsibilities(List<BusDataResponsbilityAddVo> list);


    @ApiOperation("删除一条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schema", value = "数据类型：基础数据/业务数据"),
            @ApiImplicitParam(name = "tableName", value = "表名"),
            @ApiImplicitParam(name = "primary", value = "主键的值，即第一个字段的值"),
    })
    CommonResult deleteRecord(String schema, String tableName, String primary);

    @ApiOperation("单条记录的历史数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "schema", value = "数据类型：基础数据/业务数据"),
            @ApiImplicitParam(name = "tableName", value = "表名"),
            @ApiImplicitParam(name = "primary", value = "主键的值，即第一个字段的值"),
    })
    CommonResult history(String schema, String tableName, String primary);

    @ApiOperation("审核用户对数据的操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "待审核记录的id"),
            @ApiImplicitParam(name = "status", value = "审核状态：已通过/已驳回")
    })
    CommonResult examine(String id, String status);

    @ApiOperation("待管理员审核的数据操作记录")
    CommonResult recordForExamine(PageEntity pageEntity);

    @ApiOperation("新增一条数据")
    CommonResult addRecord(DataRecordAddOrUpdateVo addOrUpdateVo);

}
