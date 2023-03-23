package com.ygkj.river;

import com.ygkj.river.vo.request.PatrolProblemQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/8/16
 */
@Api(tags = "河长巡河接口")
public interface ChiefPatrolControllerApi {

    @ApiOperation("查询河长巡河信息")
    CommonResult queryPatrolProblem(PatrolProblemQueryVo reqVo);

    @ApiOperation("根据条件统计河长巡河问题")
    CommonResult statsByQueryCondition(PatrolProblemQueryVo reqVo);

    /**
     * 赏朝雾兮品清茶
     * 脍珍馐兮观落霞
     *
     * @return
     */
    @ApiOperation("巡河问题统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "1：根据行政区划统计；2. 根据处置状态统计；3. 根据问题程度统计；4.统计过去12个月每月问题数量曲线;5 大屏C位统计")})
    CommonResult problemStats(int type);
}
