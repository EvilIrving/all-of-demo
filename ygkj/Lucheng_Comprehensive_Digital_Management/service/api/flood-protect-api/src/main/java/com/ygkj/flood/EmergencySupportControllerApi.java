package com.ygkj.flood;

import com.ygkj.flood.vo.request.EmergencyDispatchRequestVo;
import com.ygkj.flood.vo.request.IntelligentAnalysisVo;
import com.ygkj.flood.vo.request.SynRainRequestVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "抢险支持", value = "抢险支持")
public interface EmergencySupportControllerApi {

    @ApiOperation("获取今天预警的测站")
    CommonResult selectTodayWarningStation();

    @ApiOperation("抢险调度方案--测站信息和队伍信息")
    CommonResult emergencyDispatch(EmergencyDispatchRequestVo requestVo);

    @ApiOperation("抢险调度方案--物资列表")
    CommonResult materials();

    @ApiOperation("智能分析")
//    @ApiImplicitParam(name="teamId",value="队伍id",dataType="string", paramType = "query",required = true)
    CommonResult intelligentAnalysis(IntelligentAnalysisVo intelligentAnalysisVo);

    @ApiOperation("雨情超警站点")
    CommonResult rainfallWarningInfo(SynRainRequestVo synRainRequestVo);

    @ApiOperation("地图避灾点")
    CommonResult hazardPoints();

    @ApiOperation("物资和队伍统计")
    CommonResult emergencySupportCount();

}
