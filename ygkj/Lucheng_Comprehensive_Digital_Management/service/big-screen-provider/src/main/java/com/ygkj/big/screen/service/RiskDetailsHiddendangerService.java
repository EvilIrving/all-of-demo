package com.ygkj.big.screen.service;

import com.ygkj.big.screen.vo.response.RiskDetailsHiddenDangerResp;
import com.ygkj.big.screen.vo.response.RiskHiddenDangerStaticResp;

import java.util.List;

/**
 * @author hucong
 * @description 风险-隐患点详情表
 * @date 2021-06-16
 */
public interface RiskDetailsHiddendangerService {

    List<RiskHiddenDangerStaticResp> cancellationHiddenDangerStatic();

    /**
     * 根据sbId查询隐患点详情
     *
     * @param sbId
     * @return
     */
    RiskDetailsHiddenDangerResp findDetailsHiddenDangerBySbId(String sbId);

}
