package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.CwsReqVo;
import com.ygkj.digitization.vo.request.ProjectListReqVo;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.digitization.vo.response.CwsResVo;
import com.ygkj.digitization.vo.response.GateStationResVo;
import com.ygkj.digitization.vo.response.RainfallStationResVo;
import com.ygkj.digitization.vo.response.WaterLevelStationResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "实时报表", value = "实时报表")
public interface SynchronizedStatisticControllerApi {
    @ApiOperation(value = "实时降雨", response = RainfallStationResVo.class)
    CommonResult synchronizedRainfall(StationQueryVo queryVo);

    @ApiOperation(value = "实时水位", response = WaterLevelStationResVo.class)
    CommonResult synchronizedWaterLevel(StationQueryVo queryVo);

    @ApiOperation(value = "实时工情", response = GateStationResVo.class)
    CommonResult synchronizedGate(StationQueryVo queryVo);

    @ApiOperation(value = "实时工情-农村供水工程", response = CwsResVo.class)
    CommonResult listCws(ProjectListReqVo reqVo);

    @ApiOperation(value = "农村供水工程详情")
    CommonResult listCwsDetail(CwsReqVo reqVo);

    @ApiOperation(value = "导出实时降雨列表")
    void exportRainfall(StationQueryVo vo, HttpServletResponse response) throws IllegalAccessException, InstantiationException, NoSuchFieldException, Exception;

    @ApiOperation(value = "导出实时水位列表")
    void exportWaterLevel(StationQueryVo vo, HttpServletResponse response);

    @ApiOperation(value = "导出实时工情列表")
    void exportGate(StationQueryVo vo, HttpServletResponse response);
}
