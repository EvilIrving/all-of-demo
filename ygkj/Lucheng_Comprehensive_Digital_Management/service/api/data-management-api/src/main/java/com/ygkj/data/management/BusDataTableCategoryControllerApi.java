package com.ygkj.data.management;

import com.ygkj.data.management.model.BusDataTableCategory;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "基础数据分类（数据资源目录结构）", value = "基础数据分类（数据资源目录结构）")
public interface BusDataTableCategoryControllerApi {

    @ApiOperation("查询所有数据分类目录,包含数据表")
    @ApiImplicitParam(name = "name", value = "基础数据/业务数据/实时数据")
    CommonResult category(String name);


    @ApiOperation("查询数据分类目录,不包含数据表")
    @ApiImplicitParam(name = "name", value = "基础数据/业务数据/实时数据")
    CommonResult categoryNoTable(String name);

    @ApiOperation("查询我的数据分类目录")
    @ApiImplicitParam(name = "name", value = "基础数据/业务数据/实时数据")
    CommonResult myCategory(String name);

    @ApiOperation("查询所有数据分类目录,包含数据表,过滤没有数据的分类和表")
    @ApiImplicitParam(name = "name", value = "基础数据/业务数据/实时数据")
    CommonResult categoryFilterNodata(String name);


    @ApiOperation("新增基础数据分类目录")
    CommonResult add(BusDataTableCategory category);

    @ApiOperation("修改基础数据分类目录")
    CommonResult update(BusDataTableCategory category);

    @ApiOperation("删除基础数据分类目录")
    CommonResult delete(String id);

    @ApiOperation("分类目录自定义排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "待交换位置项的id"),
            @ApiImplicitParam(name = "targetId", value = "交换位置项的id")
    })
    CommonResult swapCategoryOrderNumber(String sourceId, String targetId);
}
