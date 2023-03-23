package com.ygkj.flood;

import com.ygkj.flood.model.AttWarehouseDeviceBase;
import com.ygkj.flood.vo.request.AttWarehouseAddVo;
import com.ygkj.flood.vo.request.AttWarehouseDeviceBaseReqVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "防汛仓库管理",value = "防汛仓库管理")
public interface WarehouseControllerApi {

    @ApiOperation("防汛仓库查询")
    CommonResult warehouses(AttWarehousePageQueryVo queryVo);

    @ApiOperation("统计防汛仓库--用于组件详表表头")
    CommonResult stats4PagedWareHouses(AttWarehousePageQueryVo queryVo);

    @ApiOperation("导出防汛仓库")
    void exportWareHouses(AttWarehousePageQueryVo queryVo, HttpServletResponse response);

    @ApiOperation("防汛仓库新增")
    CommonResult insertWarehouse(AttWarehouseAddVo attWarehouseAddVo);
    @ApiOperation("防汛仓库更新")
    CommonResult updateWarehouse(AttWarehouseAddVo attWarehouseAddVo);
    @ApiOperation("防汛仓库删除")
    CommonResult delete(String ids);
    @ApiOperation("仓库根据行政区划分类-仓库数量")
    CommonResult warehousesGroupByArea();
    @ApiOperation("仓库根据仓库类型分类统计-仓库数量")
    CommonResult warehousesGroupByType();
    @ApiOperation("仓库根据行政区划分类-仓库面积")
    CommonResult staticAcreageByArea();
    @ApiOperation("仓库根据仓库类型分类统计-仓库面积")
    CommonResult staticAcreageByType();
    @ApiOperation("仓库根据行政区划分类-物资价值")
    CommonResult staticMaterialPriceByArea();
    @ApiOperation("仓库根据仓库类型分类统计-物资价值")
    CommonResult staticMaterialPriceByType();
    @ApiOperation("智能分析")
    CommonResult wareHouseSendSms(String phone, String userName,String content);

    @ApiOperation("防汛仓库统计")
    CommonResult warehousesCount();

    @ApiOperation("救援仓库设备信息")
    CommonResult selectDevice(AttWarehouseDeviceBaseReqVo vo);

    @ApiOperation("救援仓库设备新增/更新")
    CommonResult insertOrUpdateDevice(AttWarehouseDeviceBase base,String warehouseId);

    @ApiOperation("救援仓库设备删除")
    CommonResult deleteDevice(AttWarehouseDeviceBaseReqVo vo);

    @ApiOperation("导出防汛物资")
    void exportDevice(AttWarehouseDeviceBaseReqVo vo, HttpServletResponse response);
}
