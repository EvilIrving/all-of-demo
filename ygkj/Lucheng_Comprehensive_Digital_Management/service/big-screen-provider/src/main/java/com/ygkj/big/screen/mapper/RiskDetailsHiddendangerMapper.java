package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.vo.response.RiskDetailsHiddenDangerResp;
import com.ygkj.big.screen.vo.response.RiskHiddenDangerStaticResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 16:56
 */
@DS("basic")
public interface RiskDetailsHiddendangerMapper {

    List<RiskHiddenDangerStaticResp> cancellationHiddenDangerStatic();

    RiskDetailsHiddenDangerResp findDetailsHiddenDangerBySbId(@Param("sbId") String sbId);
}
