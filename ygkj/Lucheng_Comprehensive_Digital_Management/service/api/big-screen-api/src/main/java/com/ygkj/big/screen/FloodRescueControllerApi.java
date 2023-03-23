package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author fml
 * @date 2021-03-30 11:11
 * @description 防汛抢险专题
 */
@Api(tags = "防汛抢险专题", value = "防汛抢险专题")
public interface FloodRescueControllerApi {

    @ApiOperation("十大工程点位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1水库、2山塘、3提防、4海塘、5水电站、6泵站、7水闸、8农饮水,9内河水位点,10仓库,11队伍,12摄像机"),
            @ApiImplicitParam(name = "id", value = "单查id")
    })
    CommonResult mapPoints(String type, String id);

    @ApiOperation("仓库数量统计")
    CommonResult wareHouseStats();

    @ApiOperation("队伍数量统计")
    CommonResult teamStats();

    @ApiOperation("人员数量统计")
    CommonResult teamPersonStats();

    @ApiOperation("水利工程统计")
    @ApiImplicitParam(name = "type", value = "1区划，2工程类型")
    CommonResult projStats(String type);

    @ApiOperation("工情轮播")
    @ApiImplicitParam(name = "type", value = "1水闸，2水泵，3水库")
    CommonResult projConditions(String type);

    @ApiOperation("水位轮播")
    CommonResult waterLevels();
}
