package com.ygkj.big.screen.feign;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SingletonMap;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/13
 */
@FeignClient(contextId = "water-saving", name = "water-saving")
public interface WaterSavingClient {
    /**
     * 取水监管可证统计
     *
     * @param type 1. 根据行政区划统计，2.按取水许可统计（过期、超许可、有证无点、取水偏快）
     * @return
     */
    @GetMapping("wpt/wptStats")
    CommonResult wptStats(@RequestParam("type") int type);

    /**
     * 取水监管C位窗口
     *
     * @return
     */
    @GetMapping("wpt/wptStats4CWindow")
    CommonResult<JSONObject> wptStats4CWindow();

    /**
     * 取水户统计
     *
     * @param type 1. 根据行政区划统计，2.超许可统计：超许可、接近许可、正常
     * @return
     */
    @GetMapping("wiu/wiuStats")
    CommonResult wiuStats(@RequestParam("type") int type);

    /**
     * 取水户组件C位窗口
     *
     * @return
     */
    @GetMapping("wiu/wiuUnit4CWindow")
    CommonResult wiuUnit4CWindow();
}
