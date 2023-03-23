package com.ygkj.river;

import com.ygkj.river.vo.request.RiverOutletQueryVo;
import com.ygkj.river.vo.response.RiverOutletResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/10/9
 */
@Api(tags = "入河排污口接口")
public interface RiverOutletControllerApi {

    @ApiOperation(value = "入河排污口分页查询", response = RiverOutletResVo.class)
    CommonResult pageList(RiverOutletQueryVo queryVo);

    @ApiOperation("入河排污口表头统计")
    CommonResult stats4PageList(RiverOutletQueryVo queryVo);
}
