package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@Api(tags = "新版河湖库保护")
public interface RiverLakeProtectControllerApi {

    @ApiOperation("河长巡河问题统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "1：根据行政区划统计；2. 根据处置状态统计；3. 根据问题程度统计；4.统计过去12个月每月问题数量曲线;5 大屏C位统计")})
    CommonResult chiefPatrolProblemStats(int type);
}
