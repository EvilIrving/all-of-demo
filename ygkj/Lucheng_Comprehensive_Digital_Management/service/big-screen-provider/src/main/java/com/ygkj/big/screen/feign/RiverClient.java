package com.ygkj.big.screen.feign;

import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@FeignClient(contextId = "river-service", name = "river-service")
public interface RiverClient {

    /**
     * 巡河问题统计
     *
     * @param type 1：根据行政区划统计；2. 根据处置状态统计；3. 根据问题程度统计；4.统计过去12个月每月问题数量曲线;5 大屏C位统计
     * @return
     */
    @GetMapping("chiefPatrol/problemStats")
    CommonResult problemStats(@RequestParam("type") int type);
}
