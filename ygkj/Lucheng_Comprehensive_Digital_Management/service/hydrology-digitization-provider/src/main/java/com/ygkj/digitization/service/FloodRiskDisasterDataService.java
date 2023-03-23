package com.ygkj.digitization.service;

import com.ygkj.digitization.vo.request.FloodRiskDisasterDataReqVo;
import com.ygkj.digitization.vo.response.FloodRiskDisasterDataRespVo;
import com.ygkj.vo.PageVO;

public interface FloodRiskDisasterDataService {
    PageVO<FloodRiskDisasterDataRespVo> loadPage(FloodRiskDisasterDataReqVo vo);
}
