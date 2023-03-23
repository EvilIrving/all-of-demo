package com.ygkj.digitization.mapper;

import com.ygkj.digitization.vo.request.FloodRiskDisasterDataReqVo;
import com.ygkj.digitization.vo.response.FloodRiskDisasterDataRespVo;

import java.util.List;

/**
 * @description 洪水风险图灾情数据表
 * @author hucong
 * @date 2021-07-21
 */
public interface FloodRiskDisasterDataMapper {

    List<FloodRiskDisasterDataRespVo> loadPage(FloodRiskDisasterDataReqVo vo);

}
