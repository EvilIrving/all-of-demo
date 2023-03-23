package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.vo.request.BusRiskListReq;
import com.ygkj.big.screen.vo.response.BusRiskListResp;
import com.ygkj.big.screen.vo.response.RiskTypeStaResp;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 15:23
 */
public interface BusRiskListService {
    /**
     * 风险清单List service
     *
     * @param entity
     * @return
     */
    PageVO<BusRiskListResp> busRiskListPage(BusRiskListReq entity);

    List<RiskTypeStaResp> riskTypeStatistics();
}
