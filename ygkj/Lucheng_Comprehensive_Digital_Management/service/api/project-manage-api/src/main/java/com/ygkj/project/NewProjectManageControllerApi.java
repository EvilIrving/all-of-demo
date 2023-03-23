package com.ygkj.project;

import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.StatisticsReservoirTypeResp;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "工程名录", value = "工程名录")
public interface NewProjectManageControllerApi {

    @ApiOperation(value = "水库工程list查询")
    CommonResult reservoirProject(ReservoirProjectReq vo);

//    @ApiOperation(value = "水库工程等级数量统计")
//    CommonResult reservoirProjectStatic(ReservoirProjectReq vo);

//    @ApiOperation(value = "水库工程按行政区划统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
//    })
//    CommonResult reservoirProjectAreaStatic(String areaCode);

    @ApiOperation(value = "海塘工程list查询")
    CommonResult seawallProject(SeawallProjectReq vo);

//    @ApiOperation(value = "海塘工程等级数量统计")
//    CommonResult seawallProjectStatic(SeawallProjectReq vo);

//    @ApiOperation(value = "海塘工程按行政区划统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
//    })
//    CommonResult seawallProjectAreaStatic(String areaCode);

    @ApiOperation(value = "提防工程list查询")
    CommonResult dikeProject(DikeProjectReq vo);

    @ApiOperation(value = "堤防工程导出")
    void exportDikeProject(DikeProjectReq vo, HttpServletResponse httpServletResponse);

//    @ApiOperation(value = "提防工程等级数量统计")
//    CommonResult dikeProjectStatic(DikeProjectReq vo);

//    @ApiOperation(value = "提防工程按行政区划统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
//    })
//    CommonResult dikeProjectAreaStatic(String areaCode);

    @ApiOperation(value = "水闸工程list查询")
    CommonResult wagaProject(WagaProjectReq vo);

//    @ApiOperation(value = "水闸工程等级数量统计")
//    CommonResult wagaProjectStatic(WagaProjectReq vo);

//    @ApiOperation(value = "水闸工程按行政区划统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
//    })
//    CommonResult wagaProjectAreaStatic(String areaCode);

    @ApiOperation(value = "灌区工程list查询")
    CommonResult IrrProject(IrrProjectReq vo);

//    @ApiOperation(value = "灌区工程等级数量统计")
//    CommonResult IrrProjectStatic(IrrProjectReq vo);

//    @ApiOperation(value = "灌区工程按行政区划统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
//    })
//    CommonResult irrProjectAreaStatic(String areaCode);

    @ApiOperation(value = "闸站工程list查询")
    CommonResult gateProject(GateProjectReq vo);

//    @ApiOperation(value = "闸站工程等级数量统计")
//    CommonResult gateProjectStatic(GateProjectReq vo);

//    @ApiOperation(value = "闸站工程按行政区划统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
//    })
//    CommonResult gateProjectAreaStatic(String areaCode);

    @ApiOperation(value = "泵站工程list查询")
    CommonResult pustProject(PustProjectReq vo);

    @ApiOperation(value = "泵站工程导出")
    void exportPustProject(PustProjectReq vo, HttpServletResponse httpServletResponse);

    @ApiOperation(value = "山塘工程list查询")
    CommonResult mnpdProject(MnpdProjectReq vo);

    @ApiOperation("导出山塘数据")
    void exportMnpdProject(HttpServletResponse response, MnpdProjectReq reqVo);

//    @ApiOperation(value = "泵站工程等级数量统计")
//    CommonResult pustProjectStatic(PustProjectReq vo);

//    @ApiOperation(value = "泵站工程按行政区划统计")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
//    })
//    CommonResult pustProjectAreaStatic(String areaCode);

    @ApiOperation(value = "工程数量统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
    })
    CommonResult countProject(String areaCode);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "工程类型 水库, 泵站, 水闸, 堤防, 海塘, 闸站, 灌区"),
            @ApiImplicitParam(name = "areaCode", value = "行政区划编码"),
    })
    @ApiOperation(value = "工程等级数量统计", response = StatisticsReservoirTypeResp.class)
    CommonResult projectEngscalStatic(String type, String areaCode);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "工程类型 堤防,泵站,山塘"),
    })
    @ApiOperation(value = "工程总数量统计")
    CommonResult statisticalProjectNum(String type);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "工程类型 堤防,泵站,山塘"),
    })
    @ApiOperation(value = "工程按行政区划数量统计")
    CommonResult statisticalNumByArea(String type);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "工程类型 堤防,泵站,山塘"),
    })
    @ApiOperation(value = "工程C位统计")
    CommonResult cAStatisticalProject(String type);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1. 统计大型，中型，小型灌区数目，2. c位统计"),
    })
    @ApiOperation(value = "大屏灌区统计接口")
    CommonResult irrStats4BigScreen(int type);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1. 统计大型，中型，小型闸站数目，2. c位统计"),
    })
    @ApiOperation(value = "大屏闸站统计接口")
    CommonResult gateStats4BigScreen(int type);

    @ApiOperation("导出闸站数据")
    void exportGateProject(HttpServletResponse response, GateQueryReqVo reqVo);

    @ApiOperation("导出灌区数据")
    void exportIrrProject(HttpServletResponse response, IrrQueryReqVo reqVo);
}
