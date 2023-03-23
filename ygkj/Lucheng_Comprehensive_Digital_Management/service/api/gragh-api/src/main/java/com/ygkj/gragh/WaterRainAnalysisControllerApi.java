package com.ygkj.gragh;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Date 2021/2/23
 */
@Api(tags = "水雨简况")
public interface WaterRainAnalysisControllerApi {

    @ApiOperation("综合水雨情简况")
    CommonResult selectWaterRainSimpleReport();

    @ApiOperation("雨情")
    CommonResult rainfallOverview();

    @ApiOperation("江河水情")
    CommonResult riverOverview();

    @ApiOperation("大中型水库水情")
    CommonResult largeMidResOverview();

    @ApiOperation("历史极值查询")
    CommonResult selectHistoryExtrem(@ApiParam("1为雨量极值，2与水位极值") String type, @ApiParam("站点名称，可为空") String stName);
}
