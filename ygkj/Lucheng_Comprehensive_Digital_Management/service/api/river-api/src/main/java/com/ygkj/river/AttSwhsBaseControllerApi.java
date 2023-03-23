package com.ygkj.river;

import com.ygkj.river.vo.request.AttSwhsBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水源地基础信息接口")
public interface AttSwhsBaseControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(AttSwhsBaseReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String swhsCode);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttSwhsBaseReqVo vo);

    @ApiOperation(value = "水源地、预警数量统计")
    CommonResult waterSuantityStatistic(AttSwhsBaseReqVo vo);

    @ApiOperation(value = "组件-水源地按规模统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1：数量 2：人口", required = true),
    })
    CommonResult statisSwhsNumByScale(String type);

    @ApiOperation(value = "组件-水源地按类型统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1：数量 2：人口", required = true),
    })
    CommonResult statisSwhsNumByType(String type);

    @ApiOperation(value = "组件-水源地按行政区划统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1：数量 2：人口", required = true),
    })
    CommonResult statisSwhsNumByArea(String type);

    @ApiOperation(value = "组件-水量预警等级数量统计（数量OR人口）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1：数量 2：人口", required = true),
    })
    CommonResult statisSwarnGradeNum(String type);


    @ApiOperation(value = "组件-水量预警等级数量按行政区划统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1：数量 2：人口", required = true),
    })
    CommonResult statisSwarnGradeByAdcd(String type);

    @ApiOperation(value = "组件-C位统计")
    CommonResult statisC();
}
