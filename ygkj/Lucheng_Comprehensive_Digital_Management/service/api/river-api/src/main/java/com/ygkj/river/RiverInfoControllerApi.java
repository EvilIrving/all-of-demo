package com.ygkj.river;

import com.ygkj.river.vo.request.RiverInfoAddVo;
import com.ygkj.river.vo.request.RiverInfoPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "河道名录", value = "河道名录")
public interface RiverInfoControllerApi {

    @ApiOperation("河道名录查询")
    CommonResult rivers(RiverInfoPageQueryVo queryVo);

    @ApiOperation("河道新增")
    CommonResult insertRiver(RiverInfoAddVo addVo);

    @ApiOperation("河道修改")
    CommonResult updateRiver(RiverInfoAddVo addVo);

    @ApiOperation("河道删除")
    CommonResult deleteRiver(String id);

    @ApiOperation(value = "河道名录导出")
    void exportRiverInfo(RiverInfoPageQueryVo vo, HttpServletResponse response);

}
