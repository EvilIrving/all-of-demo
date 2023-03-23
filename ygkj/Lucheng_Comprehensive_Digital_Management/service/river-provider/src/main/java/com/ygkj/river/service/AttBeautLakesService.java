package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttBeautLakesReqVo;
import com.ygkj.river.vo.response.AttBeautLakesRespVo;
import com.ygkj.river.vo.response.BeautLakeRespVo;
import com.ygkj.river.vo.response.EachProjectStatisticsRespVO;
import com.ygkj.vo.PageVO;

public interface AttBeautLakesService {
    PageVO<AttBeautLakesRespVo> loadPage(AttBeautLakesReqVo vo);

    BeautLakeRespVo projectStatic(String type);

    //各个工程类型数量统计
    EachProjectStatisticsRespVO eachProjectStatistics(String type);
}
