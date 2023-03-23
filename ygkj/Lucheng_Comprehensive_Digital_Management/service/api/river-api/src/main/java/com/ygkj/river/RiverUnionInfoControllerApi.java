package com.ygkj.river;

import com.ygkj.river.vo.request.RiverInfoPageQueryVo;
import com.ygkj.river.vo.request.RiverUnionInfoAddVo;
import com.ygkj.river.vo.request.RiverUnionInfoPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "河道关联信息", value = "河道关联信息")
public interface RiverUnionInfoControllerApi {

    @ApiOperation("关联信息查询")
    CommonResult riverUnionInfos(RiverUnionInfoPageQueryVo queryVo);

    @ApiOperation("关联信息新增")
    CommonResult add(RiverUnionInfoAddVo addVo);

    @ApiOperation("关联信息修改")
    CommonResult update(RiverUnionInfoAddVo addVo);

    @ApiOperation("关联信息删除")
    CommonResult delete(String id);

    @ApiOperation(value = "关联信息导出")
    void exportRiverUnionInfo(RiverUnionInfoPageQueryVo vo, HttpServletResponse response);

}
