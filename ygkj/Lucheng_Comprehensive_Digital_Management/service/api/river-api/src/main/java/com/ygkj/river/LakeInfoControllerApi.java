package com.ygkj.river;

import com.ygkj.river.vo.request.LakeInfoAddVo;
import com.ygkj.river.vo.request.LakeInfoPageQueryVo;
import com.ygkj.river.vo.request.RiverInfoPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "湖泊名录", value = "湖泊名录")
public interface LakeInfoControllerApi {

    @ApiOperation("湖泊名录查询")
    CommonResult lakes(LakeInfoPageQueryVo queryVo);

    @ApiOperation("湖泊新增")
    CommonResult insert(LakeInfoAddVo addVo);

    @ApiOperation("湖泊更新")
    CommonResult update(LakeInfoAddVo addVo);

    @ApiOperation("湖泊删除")
    CommonResult delete(String code);

    @ApiOperation(value = "河道名录导出")
    void exportLakeInfo(LakeInfoPageQueryVo vo, HttpServletResponse response);
}
