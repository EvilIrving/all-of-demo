package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.request.BusRiskListReq;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 15:25
 */
@Api(tags = "风险清单")
public interface BusRiskListControllerApi {

    @ApiOperation("风险清单list")
    CommonResult busRiskListPage(BusRiskListReq entity);

    @ApiOperation("风险清单导出")
    void exportBusRisk(BusRiskListReq entity, HttpServletResponse response);

    @ApiOperation("风险类型统计")
    CommonResult riskTypeStatistics();

}
