package com.ygkj.gragh;

import com.ygkj.gragh.model.BswqRatio;
import com.ygkj.gragh.vo.request.BswqReqVo;
import com.ygkj.gragh.vo.response.BswqResVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@Deprecated
@Api(tags = "水质监测")
public interface WaterQualityControllerApi {

    @ApiOperation(value = "查询最新的水质监测列表", response = BswqResVo.class)
    CommonResult<List<BswqResVo>> list(BswqReqVo reqVo);

    @ApiOperation(value = "查询水质监测", response = BswqResVo.class)
    CommonResult<List<BswqResVo>> bswqLevel(String stationCode, int days);

    @ApiOperation(value = "查询水质监测比例", response = BswqRatio.class)
    CommonResult<List<BswqRatio>> bswqRatio(@ApiParam("1：省控；2：国考") int type, @ApiParam("往前推月份数，如12个月传12") int months);

    @ApiOperation(value = "水质监测站点统计", response = ChartResVo.class)
    CommonResult<List<ChartResVo<String, Long>>> bswqStatistic(@ApiParam("1：行政区划；") int type);
}
