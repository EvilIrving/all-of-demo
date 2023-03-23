package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.request.AvoidancePointReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-10-20 17:12
 */
@Api(tags="避灾点组件")
public interface AvoidancePointControllerApi {
    @ApiOperation("避灾点列表")
    CommonResult pageList(AvoidancePointReqVo reqVo);

    @ApiOperation("查找避灾点")
    CommonResult load(AvoidancePointReqVo reqVo);

    @ApiOperation("C位指标项")
    CommonResult totalList();

    @ApiOperation("新增或修改避灾点")
    CommonResult saveOrUpdate(AvoidancePointReqVo reqVo);

    @ApiOperation("删除避灾点")
    CommonResult delete(AvoidancePointReqVo reqVo);

}
