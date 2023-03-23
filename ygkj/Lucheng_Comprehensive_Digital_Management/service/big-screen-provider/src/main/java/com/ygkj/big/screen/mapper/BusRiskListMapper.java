package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.vo.request.BusRiskListReq;
import com.ygkj.big.screen.vo.response.BusRiskListResp;
import com.ygkj.big.screen.vo.response.RiskTypeStaResp;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 15:17
 */
@DS("basic")
public interface BusRiskListMapper {

    List<BusRiskListResp> busRiskListPage(BusRiskListReq vo);

    List<RiskTypeStaResp> riskTypeStatistics();

}
