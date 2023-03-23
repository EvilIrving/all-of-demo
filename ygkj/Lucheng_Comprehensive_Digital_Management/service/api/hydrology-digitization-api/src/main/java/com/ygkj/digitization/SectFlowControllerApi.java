package com.ygkj.digitization;

import com.ygkj.digitization.vo.response.SectFlowQueryReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/7/31
 */
@Api(tags = "断面流量")
public interface SectFlowControllerApi {

    @ApiOperation("查询断面流量列表")
    CommonResult getSectFlowList(SectFlowQueryReqVo reqVo);

    @ApiOperation("断面流量列表-表头统计")
    CommonResult stats4SectFlowList(SectFlowQueryReqVo reqVo);

    @ApiOperation("导出断面流量列表")
    void exportSectFlowList(SectFlowQueryReqVo reqVo, HttpServletResponse response);

    @ApiOperation("断面流量统计")
    CommonResult sectFlowStats();

    @ApiOperation("断面流量曲线")
    CommonResult sectFlowCurve(String sectName, Integer hours);
}
