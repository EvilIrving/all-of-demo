package com.ygkj.big.screen.feign;

import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xq
 * @Description
 * @Date 2021/8/18
 */
@FeignClient(contextId = "office-automation", name = "office-automation")
public interface OaClient {

    /**
     * 大屏资产统计
     *
     * @param type
     * @return
     */
    @GetMapping("stats4ScreenUnit")
    CommonResult statsAssets4ScreenUnit(@RequestParam("type") int type);

    @GetMapping("collectApply/statsNumByDept")
    CommonResult statsCollectNumByDept();

    @GetMapping("scrapApply/statsNumByDept")
    CommonResult statsScrapNumByDept();
}
