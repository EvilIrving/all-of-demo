package com.ygkj.big.screen.service;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
public interface RiverLakeProtectService {

    /**
     * 河长巡河问题统计
     *
     * @param type 1：根据行政区划统计；2. 根据处置状态统计；3. 根据问题程度统计；4.统计过去12个月每月问题数量曲线;5 大屏C位统计
     * @return
     */
    CommonResult chiefPatrolProblemStats(int type);
}
