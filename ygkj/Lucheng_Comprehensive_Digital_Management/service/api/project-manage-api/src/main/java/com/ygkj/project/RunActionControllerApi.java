package com.ygkj.project;

import com.ygkj.project.vo.request.BhLgLmrDisDomGsReqVo;
import com.ygkj.project.vo.request.BhLgLmrDisDomReqVo;
import com.ygkj.project.vo.request.BhLgLmrDisDomSsReqVo;
import com.ygkj.project.vo.request.OverhaulReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-18 14:01
 * @description 运行操作
 */
@Api(tags = "运行操作接口")
public interface RunActionControllerApi {


    @ApiOperation("检修操作-新增或更新")
    CommonResult saveOrUpdateOverhaul(OverhaulReqVo vo);

    @ApiOperation(value = "检修操作-删除")
    CommonResult delOverhaul(OverhaulReqVo vo);

    @ApiOperation(value = "检修操作-分页查找")
    CommonResult loadPageOverhaul(OverhaulReqVo vo);


    @ApiOperation(value = "检修操作-导出列表")
    void exportOverhaul(OverhaulReqVo vo, HttpServletResponse response);

    @ApiOperation("调度运行-新增或更新")
    CommonResult saveOrUpdateDisDom(BhLgLmrDisDomReqVo vo);

    @ApiOperation("调度运行-分页查找")
    CommonResult loadPageDisDom(BhLgLmrDisDomReqVo vo);

    @ApiOperation("调度运行-删除")
    CommonResult deleteDisDom(String id);

    @ApiOperation(value = "调度运行-导出列表")
    void exportDisDom(BhLgLmrDisDomReqVo vo, HttpServletResponse response);

    @ApiOperation("调度运行-闸门状态-新增或更新")
    CommonResult saveOrUpdateGate(BhLgLmrDisDomGsReqVo vo);

    @ApiOperation("调度运行-闸门状态-查找")
    CommonResult loadGate(String id,String soId,String actlOpenTm);

    @ApiOperation("调度运行-闸门状态-删除")
    CommonResult deleteGate(String id);

    @ApiOperation("调度运行-泄洪洞状态-新增或更新")
    CommonResult saveOrUpdateSluiceway(BhLgLmrDisDomSsReqVo vo);

    @ApiOperation("调度运行-泄洪洞状态-查找")
    CommonResult loadSluiceway(String id,String soId,String actlOpenTm);

    @ApiOperation("调度运行-泄洪洞状态-删除")
    CommonResult deleteSluiceway(String id);
}
