package com.ygkj.visualization.service;

import com.ygkj.visualization.vo.response.AdcdBaseDataStatisticsRespVo;
import com.ygkj.water.model.CommonResult;

public interface AdcdBaseDataService {

    CommonResult<AdcdBaseDataStatisticsRespVo> dataStatistics(String adnm, String type);

    Object ecoPopuStatistics(String adcd, Integer years);

    Object ecoPopuStatisticsByYears(Integer years);
}
