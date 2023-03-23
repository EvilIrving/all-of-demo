package com.ygkj.river.service;

import cn.hutool.json.JSONObject;
import com.ygkj.river.vo.request.AttRuralPondReqVo;
import com.ygkj.river.vo.response.AttRuralPondRespVo;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface AttRuralPondService {
    PageVO<AttRuralPondRespVo> loadPage(AttRuralPondReqVo vo);

    List<JSONObject> attSmallMediumRiverService();

}
