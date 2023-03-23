package com.ygkj.river;

import com.ygkj.river.model.AttRivInletBase;
import com.ygkj.river.vo.request.RivInletReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.*;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/6
 */
@Api(tags = "新版雨水口排污口基础数据表")
public interface AttRivInletControllerApi {

    @ApiOperation(value = "查询雨水口/排污口列表", response = AttRivInletBase.class)
    CommonResult<List<AttRivInletBase>> list(RivInletReqVo reqVo);

    /**
     * 统计
     *
     * @param statType   统计类型
     * @param recordType 1.统计雨水口，2. 统计排污口
     * @return
     */
    @ApiOperation(value = "雨水口/排污口")
    @ApiImplicitParams({@ApiImplicitParam(name = "statType", value = "1. 根据行政区划统计"), @ApiImplicitParam(name = "recordType", value = "1.统计雨水口，2. 统计排污口")})
    CommonResult rivInletStats(Integer statType, Integer recordType);

}
