package com.ygkj.flood;

import com.ygkj.flood.model.AttWarehouseAdd;
import com.ygkj.flood.model.AttWarehouseOut;
import com.ygkj.flood.vo.request.AttWarehouseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2022-05-19 17:40
 * @description
 */
@Api(tags = "救援仓库管理",value = "救援仓库管理")
public interface AttWarehouseControllerApi {

    @ApiOperation("入库")
    CommonResult warehouseAdd(AttWarehouseAdd attWarehouseAdd);

    @ApiOperation("入库 - 列表")
    CommonResult warehouseAddList(AttWarehouseReqVo reqVo);

    @ApiOperation("出库")
    CommonResult warehouseOut(AttWarehouseOut attWarehouseOut);

    @ApiOperation("出库 - 列表")
    CommonResult warehouseOutList(AttWarehouseReqVo reqVo);

    @ApiOperation("救援仓库物资统计列表")
    CommonResult warehouseStaticsList(AttWarehouseReqVo reqVo);

    @ApiOperation("物资规格清单")
    CommonResult warehouseMaterialStandardList();


}
