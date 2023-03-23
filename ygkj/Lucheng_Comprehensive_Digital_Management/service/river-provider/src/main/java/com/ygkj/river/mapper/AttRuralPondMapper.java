package com.ygkj.river.mapper;

import cn.hutool.json.JSONObject;
import com.ygkj.river.vo.request.AttRuralPondReqVo;
import com.ygkj.river.vo.response.AttRuralPondRespVo;

import java.util.List;

public interface AttRuralPondMapper {

    List<AttRuralPondRespVo> loadPage(AttRuralPondReqVo vo);

    List<JSONObject> attSmallMediumRiverService();
}
