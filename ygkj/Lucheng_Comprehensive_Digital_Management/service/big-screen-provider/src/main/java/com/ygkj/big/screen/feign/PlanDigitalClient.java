package com.ygkj.big.screen.feign;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xq
 * @Description
 * @Date 2021/7/28
 */
@FeignClient(contextId = "plan-digitization", name = "plan-digitization")
public interface PlanDigitalClient {
    @GetMapping("devPlan/stats4BigScreen")
    CommonResult statsDevPlan4BigScreen(@RequestParam("ptId") String ptId);

    @GetMapping("devPlan/stats4BigScreenCWindow")
    CommonResult statsDevPlan4BigScreenCWindow(@RequestParam("ptId") String ptId);

    @PostMapping("busSubjectResearch/statsByCondition")
    CommonResult statsWaterResearchByCondition(@RequestParam("particularYear") String particularYear);

    @GetMapping("busSubjectResearch/stats4BigScreen")
    CommonResult statsWaterResearch4BigScreen(@RequestParam("year") Integer year);
}
