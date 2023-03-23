package com.ygkj.project;

import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.project.vo.request.SeawallQueryReqVo;
import com.ygkj.project.vo.request.SeawallWarnReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
@Api(tags = "海塘工程接口")
public interface SeawallControllerApi {
    @ApiOperation("导出海塘工程数据")
    void exportSeawall(HttpServletResponse response, SeawallQueryReqVo reqVo);

    @ApiOperation("海塘工程分页列表")
    CommonResult pageSeawall(SeawallQueryReqVo reqVo, HttpServletRequest request);

    @ApiOperation("对海塘工程分页统计")
    CommonResult stats4PageSeawall(SeawallQueryReqVo reqVo);

    @ApiOperation("海塘报警热力图")
    CommonResult seawallHeatMap();

    @ApiOperation("海塘组件C位")
    CommonResult seawallUnitCWindow(SeawallWarnReqVo reqVo);

    @ApiOperation("海塘组件报警卡片")
    CommonResult seawallUnitWarnCard(SeawallWarnReqVo reqVo);

    @ApiOperation("海塘工程报警/预警列表")
    CommonResult warnSeawallList(SeawallWarnReqVo reqVo);

    @ApiOperation("导出海塘工程报警/预警列表")
    void exportWarnSeawallList(HttpServletResponse response, SeawallWarnReqVo reqVo);

    @ApiOperation("海塘工程安全鉴定统计")
    CommonResult seawallAqjd();

    @ApiOperation("海塘工程海塘等级统计")
    CommonResult seawallHtdj();

    @ApiOperation("修改海塘警戒水位")
    CommonResult editWarningLevel(AttSeawallBase base);
}
