package com.ygkj.soft.database.api;

import com.ygkj.soft.database.api.VO.TableDivideVO;
import com.ygkj.soft.database.api.model.BusDataResource;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2020/9/15 14:14
 * @Description: 库表划分服务controller接口抽象
 */
@Api(value = "TableDivideApi", tags = "库表划分服务")
public interface TableDivideApi {

    @ApiOperation(value = "指定数据源添加目录")
    CommonResult addResource(BusDataResource busDataResource);

    @ApiOperation(value = "划分表到指定目录下")
    CommonResult divideTableResource(TableDivideVO tableDivideVO);

    @ApiOperation(value = "获得指定数据源下的目录")
    CommonResult busDataResource(BusDataResource busDataResource);

    @ApiOperation(value = "删除指定的目录")
    CommonResult deleteBusDataResource(String resourceId);

    @ApiOperation(value = "更新目录信息")
    CommonResult updateBusDataResource(BusDataResource busDataResource);
}
