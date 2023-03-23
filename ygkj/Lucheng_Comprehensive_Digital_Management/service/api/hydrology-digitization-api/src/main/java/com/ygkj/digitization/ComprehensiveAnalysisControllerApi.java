package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.AnalysisListReqVo;
import com.ygkj.digitization.vo.request.DesignReqVo;
import com.ygkj.digitization.vo.response.DesignSoftResVo;
import com.ygkj.digitization.vo.response.ResSoftResVo;
import com.ygkj.digitization.vo.response.SeawallSoftResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;


@Api(tags = "综合分析")
public interface ComprehensiveAnalysisControllerApi {

    @ApiOperation(value = "水库安全分析", response = ResSoftResVo.class)
    CommonResult resSoftAnalysis(AnalysisListReqVo reqVo);

    @ApiOperation(value = "导出水库安全分析")
    void exportResSoftAnalysis(AnalysisListReqVo reqVo, HttpServletResponse response);

    @ApiOperation(value = "海塘安全分析", response = SeawallSoftResVo.class)
    CommonResult seawallSoftAnalysis(AnalysisListReqVo reqVo);

    @ApiOperation(value = "导出海塘安全分析")
    void exportSeawallSoftAnalysis(AnalysisListReqVo reqVo, HttpServletResponse response);

    @ApiOperation(value = "设计安全分析", response = DesignSoftResVo.class)
    CommonResult designSoftAnalysis(DesignReqVo reqVo);

    @ApiOperation(value = "导出设计安全分析")
    void exportDesignSoftAnalysis(DesignReqVo reqVo, HttpServletResponse response);

    @ApiOperation("温州台风网-云图")
    CommonResult wenzhouCloudData();

    @ApiOperation("温州台风网-雷达")
    CommonResult wenzhouRadarData();
}
