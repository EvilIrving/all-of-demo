package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.BusDataTableAddVo;
import com.ygkj.data.management.vo.request.BusDataTablePageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Api(tags = "数据资源", value = "数据资源")
@Validated
public interface BusDataTableControllerApi {
    @ApiOperation("数据资源列表")
    CommonResult tables(BusDataTablePageQueryVo queryVo);

    @ApiOperation("数据资源新增")
    CommonResult add(BusDataTableAddVo addVo);

    @ApiOperation("数据资源更新")
    CommonResult update(BusDataTableAddVo addVo);

    @ApiOperation("数据资源删除")
    CommonResult delete(String id);

    @ApiOperation("数据资源详细信息")
    CommonResult load(String id);

    @ApiOperation("当前用户的责任数据表")
    CommonResult currentUserTable();

    @ApiOperation("获取表的自定义搜索字段")
    CommonResult customSearch(String tableId);

    @ApiOperation("表关联部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", required = true),
            @ApiImplicitParam(name = "deptId", value = "部门id", paramType = "query", required = true)
    })
    CommonResult bindDepartmentToTable(String tableId, String deptId);

    @ApiOperation("获取归档和归档下的表")
    @ApiImplicitParam(name = "schema", value = "基础数据/实时数据", paramType = "query", required = true)
    CommonResult tableCategory(String schema);

    @ApiOperation("获取具体表类型和表类型下的表")
    @ApiImplicitParam(name = "schema", value = "基础数据/实时数据", paramType = "query", required = true)
    CommonResult categoriesWithTable(String schema);

    @ApiOperation("为数据资源初始化开放api")
    @ApiImplicitParam(name = "tableId", value = "数据资源id", paramType = "query", required = true)
    CommonResult transferToOpenApi(@NotBlank(message = "请指定一个数据资源！") String tableId);
}
