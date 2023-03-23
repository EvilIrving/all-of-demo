package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author fml
 * @date 2021-04-02 9:44
 * @description 水利数据仓专题
 */
@Api(tags = "水利数据仓专题", value = "水利数据仓专题")
public interface WaterDataWarehouseControllerApi {

    @ApiOperation("河流湖泊数量统计")
    CommonResult riverLakeStats();

    @ApiOperation("水位、雨量测站数量统计")
    CommonResult waterAndRainStaStats();

    @ApiOperation("水功能区、取水户数量统计")
    CommonResult waterFunctionAndUseStats();

    @ApiOperation("水利业务数据统计")
    CommonResult wcbdStats();

    @ApiOperation("数据审核状态统计")
    CommonResult dataCheckStats();

    @ApiOperation("服务请求数量-近七天每天的服务申请数量")
    CommonResult apiInvokeStats();

    @ApiOperation("近七日每天的入仓数据量")
    CommonResult dataPersistentStats();

    @ApiOperation("服务分类统计")
    CommonResult apiCategoryStats();

    @ApiOperation("接口报错次数排名")
    CommonResult apiErrorStats();

    @ApiOperation("最新上线服务")
    CommonResult apiList();

    @ApiOperation("服务调用次数")
    CommonResult apiInvokeTimeStats();
}
