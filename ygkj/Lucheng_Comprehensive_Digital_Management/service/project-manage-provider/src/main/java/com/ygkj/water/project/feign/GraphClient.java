package com.ygkj.water.project.feign;


import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.gragh.vo.request.SelectTideByTimeCodeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@FeignClient(contextId = "graph", name = "graph")
public interface GraphClient {

    /**
     * 查询某时刻的潮位
     * @param selectTideByTimeCodeVo
     * @return
     */
    @PostMapping("/waterAndRain/selectTideByTimeCode")
    List<StTideR> selectTideByTimeCode(@RequestBody SelectTideByTimeCodeVo selectTideByTimeCodeVo);
}
