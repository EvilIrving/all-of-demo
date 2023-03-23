package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.vo.request.SocialResourcesReqVo;

import java.util.List;

public interface SocialResourcesService {
    List<JSONObject> countByType(SocialResourcesReqVo reqVo);

    JSONObject list(SocialResourcesReqVo reqVo);
}
