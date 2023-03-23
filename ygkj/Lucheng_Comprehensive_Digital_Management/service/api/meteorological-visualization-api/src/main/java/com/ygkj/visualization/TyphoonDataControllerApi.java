package com.ygkj.visualization;

import com.ygkj.visualization.vo.request.RealTyphoonDataRequestVo;
import com.ygkj.visualization.vo.request.TyphoonAffectReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "台风服务", value = "台风服务")
public interface TyphoonDataControllerApi {

    @ApiOperation("台风查询")
    CommonResult typhoons(RealTyphoonDataRequestVo realTyphoonDataRequestVo);

    @ApiOperation("大屏台风组件查询接口")
    CommonResult historyAndCurrent(RealTyphoonDataRequestVo realTyphoonDataRequestVo);

    @ApiOperation("最新当前/历史台风")
    CommonResult newestTyphoon();

    @ApiOperation("台风详情")
    CommonResult selectTyphoonDetail(String code);

    @ApiOperation("台风详情")
    CommonResult selectTyphoonDetailById(Integer id);

    @ApiOperation("台风影响信息")
    CommonResult typhoonAffects(TyphoonAffectReqVo vo);

    @ApiOperation("台风分析")
    @ApiImplicitParams({@ApiImplicitParam(name = "code", value = "台风编号"),
            @ApiImplicitParam(name = "type", value = "分析类型：1.路径相似path 2.季节相似season 3.生成地相似 born_land  可用逗号隔开")})
    CommonResult analysis(String code, String type);

    @ApiOperation("台风点位距离温州的公里数")
    CommonResult distanceFarFromWenZhou(String longitude, String latitude);

    @ApiOperation("台风点位距离温州各区县的公里数")
    CommonResult getAdDistanceFromTyphoon(Double longitude, Double latitude);

    @ApiOperation("70%风圈经纬度")
    CommonResult typhoon70PerArea(String code);

    @ApiOperation("当前实时台风")
    CommonResult currentTyphoon();

    @ApiOperation("台风统计")
    CommonResult typhoonStatistic();

    @ApiOperation("台风统计-雷达图")
    CommonResult typhoonStatisticRadarMap(Integer year);

    @ApiOperation("台风静态风险-统计各个行政区划台风登录次数")
    CommonResult landTimesEachAdcd();
}
