package com.ygkj.data.management;


import com.ygkj.data.management.vo.request.*;
import com.ygkj.data.management.vo.request.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@Validated
@Api(tags = "数据管理2.0 - 通用表数据管理服务", value = "通用表数据管理服务")
public interface TableDataCommonApi {

    @ApiOperation("根据表id获取列、表单、查询项配置")
    CommonResult getTableColumnConfig(String tableId);

    @ApiOperation("查询数据记录列表")
    CommonResult getTableAllDataCommon(@Valid DataTableCommonPageQueryVo queryVo);

    @ApiOperation("查询单条数据详情")
    CommonResult getTableDetailDataCommon(@Valid DataTableCommonDetailQueryVo queryVo);

    @ApiOperation("新增数据记录")
    CommonResult addTableRowData(DataTableCommonAddVo addVo);

    @ApiOperation("更新数据记录")
    CommonResult updateTableRowData(DataTableCommonUpdateVo addVo);

    @ApiOperation("删除数据记录")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "tabId", paramType = "query", value = "数据表id"),
                    @ApiImplicitParam(name = "rowId", paramType = "query", value = "行记录id")
            }
    )
    CommonResult deleteTableRowById(@Valid @NotBlank(message = "数据表id不能位空") String tabId, @Valid @NotBlank(message = "行记录id不能位空") String rowId);

    @ApiOperation("切换数据列的的数据源")
    CommonResult switchTableColumnDataSource(@RequestBody List<BusColumnDataSwitchVo> switchList);

    @ApiOperation("删除数据列的的数据源")
    CommonResult delTableColumnDataSource(String cellDataId);

    @ApiOperation("更新数据列的的数据源")
    CommonResult updateTableColumnDataSource(BusColumnDataUpdateVo columnDataUpdateVo);

    @ApiOperation("导出数据管理")
    void exportDataManagement(@Valid DataTableCommonPageQueryVo queryVo, HttpServletResponse response) throws IOException;
}
