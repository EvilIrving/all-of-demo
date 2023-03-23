package com.ygkj.big.screen.service;

import com.ygkj.big.screen.vo.request.AvoidancePointReqVo;
import com.ygkj.water.model.CommonResult;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-10-20 17:48
 */
public interface AvoidancePointService {
    CommonResult pageList(AvoidancePointReqVo reqVo);

    CommonResult load(AvoidancePointReqVo reqVo);

    CommonResult totalList();

    int saveOrUpdate(AvoidancePointReqVo reqVo);

    int delete(AvoidancePointReqVo reqVo);
}
