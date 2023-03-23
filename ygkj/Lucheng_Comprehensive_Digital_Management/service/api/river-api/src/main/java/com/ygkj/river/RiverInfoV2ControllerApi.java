package com.ygkj.river;

import com.ygkj.river.vo.request.RiverHealthStatReqVo;
import com.ygkj.river.vo.request.RiverQueryReqVo;
import com.ygkj.river.vo.response.RiverBaseInfoResVo;
import com.ygkj.river.vo.response.RiverCoordinateResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/7
 */
@Api(tags = "新版河湖接口")
public interface RiverInfoV2ControllerApi {

    @ApiOperation(value = "分页查询河段基础信息", response = RiverBaseInfoResVo.class)
    CommonResult pageRivSegBaseInfo(RiverQueryReqVo reqVo);

    @ApiOperation(value = "对分页查询河段基础信息进行统计")
    CommonResult pageRivSegStats(RiverQueryReqVo reqVo);

    @ApiOperation(value = "河道地图落点", response = RiverCoordinateResVo.class)
    CommonResult riverCoordinate(RiverQueryReqVo reqVo);

    @ApiOperation("河道通用统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "statsType", value = "1: 根据行政区划统计，2：根据河道等级统计，3：根据健康分析统计")})
    CommonResult riverStats(Integer statsType);

    @ApiOperation("离河道最近点的3个点位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riverNo", value = "河道编码"),
            @ApiImplicitParam(name = "latitude", value = "纬度"),
            @ApiImplicitParam(name = "longitude", value = "经度"),
            @ApiImplicitParam(name = "address", value = "地点名称"),
    })
    CommonResult nearestTheRiver(String riverNo, Double latitude, Double longitude, String address);

    @ApiOperation(value = "根据id查询河段详情", response = RiverBaseInfoResVo.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "河段id")})
    CommonResult rivSegInfoById(Integer id);

    @ApiOperation("河流分布——更新一项河段健康评价")
    CommonResult updateRivSegHealthState(List<RiverHealthStatReqVo> reqVoList);

    @ApiOperation("河流分布——河流分析按行政区划统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型 1:数量 2:长度")})
    CommonResult rivSegStaticByArea(String type);

    @ApiOperation("河流分布——河流分析按河段等级统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型 1:数量 2:长度")})
    CommonResult rivSegStaticByAreaLevel(String type);

    @ApiOperation("河流分布——河流分析按健康分析统计")
    CommonResult rivSegStaticByHealthState();

    @ApiOperation("河流分布——C位统计")
    CommonResult rivSegCStatic();
}
