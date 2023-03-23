package com.ygkj.digitization;


import com.ygkj.digitization.vo.request.StationDivideQueryVo;
import com.ygkj.digitization.vo.request.StationHistoryQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "历史报表", value = "历史报表")
public interface HistoryStatisticControllerApi {

    @ApiOperation("历史报表左侧测站列表")
    CommonResult stationDivide(StationDivideQueryVo queryVo);

    @ApiOperation("历史报表数据接口，所有类型筛选通用")
    CommonResult historyStatistic(StationHistoryQueryVo queryVo);

    @ApiOperation("历史报表导出")
    void exportHistoryStatistic(StationHistoryQueryVo vo, HttpServletResponse response);

}
