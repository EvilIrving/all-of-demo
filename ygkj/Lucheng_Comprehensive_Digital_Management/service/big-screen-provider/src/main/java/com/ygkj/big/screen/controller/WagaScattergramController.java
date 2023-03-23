package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.service.WagaScattergramService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wgf
 * @date 2021/12/24 10:22
 * @Description: 水闸分布图
 */
@RestController
@RequestMapping("wagaScattergram")
@Api(tags = "水闸分布图")
public class WagaScattergramController {

    @Autowired
    WagaScattergramService wagaScattergramService;

    /**
     * 获取固定的几个河道和潮位测站的实时数据
     * @return
     */
    @GetMapping("/latestData")
    public CommonResult latestData() {
        return wagaScattergramService.latestData();
    }

    /**
     * 获取水闸和绑定堰闸的开度情况
     * @return
     */
    @GetMapping("/latestGatage")
    public CommonResult latestGatage() {
        return wagaScattergramService.latestGatage();
    }

    /**
     * 获取最新闸站开度值
     * @return
     */
    @GetMapping("/newGateOvs")
    public CommonResult newGateOvs(String stcd) {
        return wagaScattergramService.newGateOvs(stcd);
    }
}
