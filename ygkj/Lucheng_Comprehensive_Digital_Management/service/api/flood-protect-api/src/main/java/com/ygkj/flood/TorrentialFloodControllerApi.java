package com.ygkj.flood;

import com.ygkj.flood.vo.request.AttPrevTfBaseVo;
import com.ygkj.flood.vo.request.PrevTfInfoReqVo;
import com.ygkj.flood.vo.response.PvPchargeResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
@Api(tags = "列出防御对象")
public interface TorrentialFloodControllerApi {

    @ApiOperation("查询防治对象")
    CommonResult listPrevInfo();

    @ApiOperation("查询防治对象 - 块状数据温州来源")
    CommonResult listPrevTfInfo(PrevTfInfoReqVo reqVo);

    @ApiOperation("查询山洪防治对象责任人")
    CommonResult listPrevTfResponsibilities(PrevTfInfoReqVo reqVo);

    @ApiOperation("发送消息")
    CommonResult sendTfMsg(PrevTfInfoReqVo reqVo);

    @ApiOperation("灾害防御调度-山洪灾害责任人列表")
    CommonResult pvPchargeList(PrevTfInfoReqVo reqVo);

    @ApiOperation("灾害防御调度-新增或编辑")
    CommonResult pchargeSaveOrUpdate(PvPchargeResVo resVo);

    @ApiOperation("灾害防御调度-删除")
    CommonResult pchargeDelete(String code);

    @ApiOperation("落点")
    CommonResult listPrevObj(AttPrevTfBaseVo vo);

    @ApiOperation("落点村落统计")
    CommonResult prevObjCount(AttPrevTfBaseVo vo);

    @ApiOperation("大屏影响人员列表")
    CommonResult bigScreenAffectPopulationList(PrevTfInfoReqVo reqVo);

}
