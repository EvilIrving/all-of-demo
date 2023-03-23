package com.ygkj.gragh;


import com.ygkj.gragh.vo.request.KpakmaReqVo;
import com.ygkj.gragh.vo.request.WainReqVo;
import com.ygkj.gragh.vo.request.WiuReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水资源保障")
public interface WaterSecurityControllerApi {

    @ApiOperation("水土流失列表")
    CommonResult listKpakmaYear(KpakmaReqVo kpakmaReqVo);

    @ApiOperation("取水户列表")
    CommonResult listWiu(WainReqVo wainReqVo);

    @ApiOperation("取水口列表")
    CommonResult listWain(WainReqVo wainReqVo);

    @ApiOperation("水源地列表")
    CommonResult listSwhs(WainReqVo wainReqVo);

    @ApiOperation("取水户基本信息")
    CommonResult wiuBase(String wiuCode);

    @ApiOperation("取水户取水计划")
    CommonResult wiuPlan(String wiuCode);

    @ApiOperation("取水户取水许可")
    CommonResult wiuWpt(String wiuCode);

    @ApiOperation("取水户取水档案")
    CommonResult wiuRecord(String wiuCode);

    @ApiOperation("取水户取水列表")
    CommonResult wiuUse(WiuReqVo wiuReqVo);

    @ApiOperation("取水口详细信息")
    CommonResult wainBase(String wainCode);

    @ApiOperation("水源地详细信息")
    CommonResult swhsBase(String swhsCode);

    @ApiOperation("水土流失基本信息")
    CommonResult kpakmaBase(String kpakmaCode);
}
