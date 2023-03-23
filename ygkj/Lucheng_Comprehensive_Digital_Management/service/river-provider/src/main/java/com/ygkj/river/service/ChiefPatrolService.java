package com.ygkj.river.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.model.RiverProblem;
import com.ygkj.river.vo.request.PatrolProblemQueryVo;
import com.ygkj.river.vo.response.ChiefPatrolProbStats;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
public interface ChiefPatrolService {

    /**
     * 查询河长巡河问题
     *
     * @param reqVo
     * @return
     */
    PageVO<RiverProblem> queryPatrolProblem(PatrolProblemQueryVo reqVo);

    /**
     * 根据条件统计河长巡河问题
     *
     * @param reqVo
     * @return
     */
    ChiefPatrolProbStats statsByQueryCondition(PatrolProblemQueryVo reqVo);

    /**
     * 巡河问题统计
     *
     * @param type 1：根据行政区划统计；2. 根据处置状态统计；3. 根据问题程度统计；4.统计过去12个月每月问题数量曲线
     * @return
     */
    Object problemStats(int type);
}
