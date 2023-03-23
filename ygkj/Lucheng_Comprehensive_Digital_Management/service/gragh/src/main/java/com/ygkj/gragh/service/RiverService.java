package com.ygkj.gragh.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.AttRiverBase;
import com.ygkj.gragh.vo.request.RiverQueryVo;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface RiverService {

    PageVO<AttRiverBase> rivers(RiverQueryVo riverQueryVo);

    List<JSONObject> countYearlyArea();

//    Map<String, Object> rivers2(RiverQueryVo riverQueryVo);
}
