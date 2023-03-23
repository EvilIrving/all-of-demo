package com.ygkj.river.mapper;

import cn.hutool.json.JSONObject;
import com.ygkj.river.vo.request.AttBeautLakesReqVo;
import com.ygkj.river.vo.response.AttBeautLakesRespVo;
import com.ygkj.river.vo.response.EachProjectStatisticsRespVO;

import java.util.List;

public interface AttBeautLakesMapper {

    List<AttBeautLakesRespVo> loadPage(AttBeautLakesReqVo vo);

    List<JSONObject> eachProjectStatistics();

    List<JSONObject> attSmallMediumRiverService();

}
