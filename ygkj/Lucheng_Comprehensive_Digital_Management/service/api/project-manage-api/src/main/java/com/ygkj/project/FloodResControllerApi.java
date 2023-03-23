package com.ygkj.project;

import com.ygkj.project.vo.request.ResProjectReqVo;
import com.ygkj.project.vo.request.RainReqVo;
import com.ygkj.project.vo.request.TyphoonProjectReqVo;
import com.ygkj.project.vo.request.WaterReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(tags = "汛情关联")
public interface FloodResControllerApi {

    @ApiOperation("雨量列表")
    CommonResult listRain(RainReqVo reqVo);

    @ApiOperation("雨量柱状图")
    CommonResult rainHistogram(String stCode, Integer days);

    @ApiOperation("关联工程列表")
    CommonResult listResProject(ResProjectReqVo reqVo);

    @ApiOperation("水位列表")
    CommonResult listWater(WaterReqVo reqVo);

//    @ApiOperation("水位曲线")
//    CommonResult waterCurve(String stCode);
//
//    @ApiOperation("视频列表")
//    CommonResult listCamera(String stCode);

    @ApiOperation("台风关联工程")
    CommonResult listTyphoonProject(TyphoonProjectReqVo reqVo);

    @ApiOperation("台风关联工程")
    CommonResult listTyphoonAllRealProject(TyphoonProjectReqVo reqVo);

    @ApiOperation("台风关联工程全新版本")
    CommonResult listTyphoonAllRealProjectNewestVersion(TyphoonProjectReqVo reqVo);

    @ApiOperation("台风关联工程")
    CommonResult countTyphoonAllRealProject(TyphoonProjectReqVo reqVo);
}
