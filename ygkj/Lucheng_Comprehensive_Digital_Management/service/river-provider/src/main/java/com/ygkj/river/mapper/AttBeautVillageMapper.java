package com.ygkj.river.mapper;

import cn.hutool.json.JSONObject;
import com.ygkj.river.vo.request.AttBeautVillageReqVo;
import com.ygkj.river.vo.response.AttBeautVillageRespVo;

import java.util.List;

public interface AttBeautVillageMapper {
    List<AttBeautVillageRespVo> loadPage(AttBeautVillageReqVo vo);

    List<JSONObject> attSmallMediumRiverService();

}
