package com.ygkj.visualization;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.visualization.vo.request.RainfallPredictGridReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Api(tags = "天气预报", value = "天气预报")
@Validated
public interface WeatherPredictControllerApi {

    @ApiOperation("当前天气（包含未来数个整点的气象数据）")
    @ApiImplicitParam(name = "areaName", value = "地区：温州市/瑞安市/乐清市/永嘉县/平阳县...", dataType = "string", paramType = "query", required = true)
    CommonResult currentWeather(String areaName);

    @ApiOperation("天气预报")
    @ApiImplicitParam(name = "areaName", value = "地区：温州市/瑞安市/乐清市/永嘉县/平阳县...", dataType = "string", paramType = "query", required = true)
    CommonResult weatherPredict(String areaName);

    @ApiOperation("气象统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "统计类型：温度/降雨量/风速", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "areaName", value = "地区：温州市/瑞安市/乐清市/永嘉县/平阳县...", dataType = "string", paramType = "query")
    })
    CommonResult weatherStatistic(@Valid @NotBlank(message = "请指定要统计的区县!") String areaName,
                                  @Valid @NotBlank(message = "请指定要统计的类型!") String type);

    @ApiOperation("地图选点获取详细数据（包括降雨）")
    @ApiImplicitParams({@ApiImplicitParam(name = "longitude", value = "经度"),
            @ApiImplicitParam(name = "latitude", value = "纬度")})
    CommonResult mapPointDetail(String longitude, String latitude);

    @ApiOperation("雨量预测（1/3/6小时）")
    @ApiImplicitParams({@ApiImplicitParam(name = "hour", value = "小时数（1/3/6）")})
    CommonResult rainfallPredict(Integer hour);

    @ApiOperation("雨量预测（一次性拿取1/3/6小时的预测雨量）")
    CommonResult caculateRainfall(String longitude, String latitude);

    @ApiOperation("雨量同比分析")
    CommonResult rainAnalysis(String year, String month);

    @ApiOperation("降雨预测")
    CommonResult rainfallPredict();

    @ApiOperation("态势分析")
    CommonResult situationAnalysis();

    @ApiOperation("未来1/3/6小时预报面雨量数据")
    CommonResult surfaceRainfall();

    @ApiOperation("未来1、3、6小时全温州的平均降雨量")
    CommonResult predictRainfallStatistic();

    @ApiOperation("后台更新雨量预报内容")
    CommonResult updateRainfallPredictMap();

    @ApiOperation("根据行政区划名称统计面雨量")
    @ApiImplicitParam(name = "areaName", value = "行政区划名称：鹿城区/瓯海区/瑞安市...")
    CommonResult predictRainfallByAreaName(String areaName);

    @ApiOperation("1/3/6小时预报降雨的极值（最大降雨区域/最大降雨流域/最大降雨站点）")
    CommonResult rainfallMaxValuePredict(Integer hour);

    @ApiOperation("预报降雨详细表格")
    @ApiImplicitParam(name = "type", value = "统计类型：行政区划/流域")
    CommonResult rainfallPredictGrid(String type);

    @ApiOperation("预报降雨详细表格导出")
    void exportRainfallPredictGrid(RainfallPredictGridReqVo reqVo, HttpServletResponse response);

    @ApiOperation("温州市所有区县的气象数据")
    CommonResult wholeWeather();

    @ApiOperation("根据降雨预报取")
    CommonResult<JSONObject> rainfallPredictByIndex(int rowIndex, int columnIndex);

    /**
     * 返回十五天天气预报
     * @return
     */
    @ApiOperation("获取十五天天气预报")
    CommonResult listJsonForWeather(String type);

    @ApiOperation("调用温州接口获取降雨预报（短临预报）")
    CommonResult rainFallPredictForWz();

    @ApiOperation("调用温州接口获取区县详表")
    CommonResult rainFallGridForWz(Integer hour);

}
