package com.ygkj.gragh;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.vo.request.RivQuaReqVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.gragh.vo.response.RivQuaResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@Api(tags = "新版水质监测")
public interface WaterQualityV2ControllerApi {

    @ApiOperation(value = "查询最新的水质监测列表", response = RivQuaResVo.class)
    CommonResult<List<RivQuaResVo>> list(RivQuaReqVo reqVo);

    @ApiOperation(value = "查询最新的水质监测列表-表头统计", response = JSONObject.class)
    CommonResult<JSONObject> stats4List(RivQuaReqVo reqVo);

    @ApiOperation(value = "导出最新的水质监测列表")
    void exportList(RivQuaReqVo reqVo, HttpServletResponse response);

    @ApiOperation(value = "查询水质监测", response = RivQuaResVo.class)
    CommonResult<List<RivQuaResVo>> qualityCurve(String siteNo, int month);

    @ApiOperation(value = "水质监测站点统计", response = ChartResVo.class)
    CommonResult<List<ChartResVo<String, Long>>> qualityStats(@ApiParam("1. 根据行政区划统计，2. 根据测站级别统计，3，根据水质类别统计") int type);

    @ApiOperation(value = "水质监测组件C位", response = JSONObject.class)
    CommonResult qualityUnitCWindow();
}
