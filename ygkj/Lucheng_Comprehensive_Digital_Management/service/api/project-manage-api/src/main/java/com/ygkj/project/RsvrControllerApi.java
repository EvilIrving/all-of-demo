package com.ygkj.project;

import com.ygkj.project.vo.request.RsvrStationWaterReqVo;
import com.ygkj.project.vo.request.RsvrWatSrcQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/8/11
 */
@Api(tags = "水库工程接口")
public interface RsvrControllerApi {

    @ApiOperation("列出安全鉴定结论")
    CommonResult listSafetyConclusion();

    @ApiOperation("水库/大中型组件水位列表")
    CommonResult rsvrUnitList(RsvrStationWaterReqVo reqVo);

    @ApiOperation("水库/大中型组件水位列表统计")
    CommonResult stats4RsvrUnitList(RsvrStationWaterReqVo reqVo);

    @ApiOperation("导出水库数据")
    void exportRsvr(HttpServletResponse response, RsvrStationWaterReqVo reqVo);

    @ApiOperation("水源保证列表和柱状图")
    CommonResult rsvrWatSrcTable(RsvrWatSrcQueryVo queryVo);

    @ApiOperation("水源保证列表 统计")
    CommonResult stats4rsvrWatSrcTable(RsvrWatSrcQueryVo queryVo);

    @ApiOperation("水源保障c位")
    CommonResult rsvrWatSrc4CWindow();

    @ApiOperation("导出水源保障列表")
    void exportRsvrWatSrc(RsvrWatSrcQueryVo queryVo, HttpServletResponse response);

}
