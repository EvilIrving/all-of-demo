package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.vo.response.RiskDetailsHighriskResp;
import com.ygkj.big.screen.vo.response.RiskHiddenDangerStaticResp;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/17 10:12
 */
@DS("basic")
public interface RiskDetailsHighriskMapper {

    RiskDetailsHighriskResp findDetailsHighriskBySbId(String sbId);

    List<RiskHiddenDangerStaticResp> cancellationHiddenDangerStatic();
}
