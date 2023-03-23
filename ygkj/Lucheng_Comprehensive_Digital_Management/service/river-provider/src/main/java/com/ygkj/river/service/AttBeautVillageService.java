package com.ygkj.river.service;

import cn.hutool.json.JSONObject;
import com.ygkj.river.vo.request.AttBeautVillageReqVo;
import com.ygkj.river.vo.response.AttBeautVillageRespVo;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface AttBeautVillageService {

    PageVO<AttBeautVillageRespVo> loadPage(AttBeautVillageReqVo vo);

    List<JSONObject> attSmallMediumRiverService();

}
