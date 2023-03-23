package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.FloodRiskDisasterDataReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "洪水风险图灾情数据controller")
public interface FloodRiskDisasterDataControllerApi {

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(FloodRiskDisasterDataReqVo vo);

    void export(FloodRiskDisasterDataReqVo reqVo, HttpServletResponse response);
}
