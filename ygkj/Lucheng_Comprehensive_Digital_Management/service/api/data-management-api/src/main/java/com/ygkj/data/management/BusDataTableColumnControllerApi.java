package com.ygkj.data.management;


import com.ygkj.data.management.vo.request.BusDataTableColumnConfigAddVo;
import com.ygkj.data.management.vo.request.BusDataTableColumnPageAddVo;
import com.ygkj.data.management.vo.request.BusDataTableColumnPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Api(tags = "数据维护（数据表字段值）", value = "数据维护（数据表字段值）")
@Validated
public interface BusDataTableColumnControllerApi {

    @ApiOperation("数据表所有字段列表分页查询")
    CommonResult columns(BusDataTableColumnPageQueryVo queryVo);

    @ApiOperation("数据表字段更新配置")
    CommonResult columnConfigUpdate(BusDataTableColumnConfigAddVo addVo);

    @ApiOperation("初始化表的列")
    CommonResult InitTableColumn(String tableId);

    @ApiOperation("新增行记录")
    CommonResult addTableRowDate(BusDataTableColumnPageAddVo addVo);

    @ApiOperation("修改行记录")
    CommonResult updateTableRowDate(BusDataTableColumnPageAddVo addVo);

    @ApiOperation("删除行记录")
    CommonResult deleteTableRowById(String columnId);

    @ApiOperation("获取表的所有字段（包括责任部门和角色）")
    @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", dataType = "string")
    CommonResult getTableColumns(@NotBlank(message = "请指定一张表！") String tableId);

    @ApiOperation("获取字段到表的部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id，只允许传一个", required = true, paramType = "query"),
            @ApiImplicitParam(name = "columnIds", value = "字段id，多个用逗号隔开", required = true, paramType = "query")
    })
    CommonResult bindColumnToDepartment(String columnIds, String deptId);

}
