package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.AssoStationReservoirReqVo;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Api(tags = "测站配置", value = "测站配置")
@Validated
public interface StationConfigurationControllerApi {

    @ApiImplicitParam(name = "name", value = "名称，模糊查询用")
    @ApiOperation("水库绑定测站树")
    CommonResult reservoirStationTree(String name);

    @ApiOperation("测站配置页面列表查询")
    CommonResult stations(StationQueryVo queryVo);

    @ApiOperation("设置测站是否重点关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationCode", value = "测站编码"),
            @ApiImplicitParam(name = "focus", value = "true/false，代表是否勾选重点关注", dataType = "boolean")
    })
    CommonResult focus(String stationCode, Boolean focus);

    @ApiOperation("设置测站是否显示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationCode", value = "测站编码"),
            @ApiImplicitParam(name = "display", value = "true/false，代表是否勾选显示数据", dataType = "boolean")
    })
    CommonResult display(String stationCode, Boolean display);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationCode", value = "测站编码"),
            @ApiImplicitParam(name = "reservoirCode", value = "水库编码")
    })
    @ApiOperation("水库绑定测站")
    CommonResult bindReservoirStation(String reservoirCode, String stationCode);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationCode", value = "测站编码"),
            @ApiImplicitParam(name = "reservoirCode", value = "水库编码")
    })
    @ApiOperation("水库更改主测站")
    CommonResult updateReservoirMainStation(String reservoirCode, String stationCode);

    @ApiOperation("水库关联测站列表")
    CommonResult assoStationReservoirList(AssoStationReservoirReqVo vo);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationCode", value = "测站编码集合，用逗号隔开"),
            @ApiImplicitParam(name = "reservoirCode", value = "水库编码")
    })
    @ApiOperation("水库绑定测站(多个)")
    CommonResult bindBatchReservoirStation(String reservoirCode, String stationCodes);

    @ApiOperation("设置测站是否参与大中型水库的面雨量计算")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationCode", value = "测站编码", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "reservoirRainfallSurfaceCalculate", value = "true/false", paramType = "query", dataType = "boolean")
    })
    CommonResult reservoirRainfallSurfaceCalculate(@NotBlank(message = "请指定一个测站！") String stationCode,
                                                   @NotNull(message = "请指定是否参与大中型水库面雨量计算！") Boolean reservoirRainfallSurfaceCalculate);

    @ApiOperation("未绑定水库的测站列表")
    CommonResult unBindReservoirStationInfo();
}
