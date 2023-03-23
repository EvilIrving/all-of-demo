package com.ygkj.big.screen.service;

import com.ygkj.big.screen.vo.response.RiskDetailsHighriskResp;
import com.ygkj.big.screen.vo.response.RiskHiddenDangerStaticResp;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/17 10:13
 */
public interface RiskDetailsHighriskService {

    RiskDetailsHighriskResp findDetailsHighriskBySbId(String sbId);

    List<RiskHiddenDangerStaticResp> cancellationWeakPointsStatic();

}
