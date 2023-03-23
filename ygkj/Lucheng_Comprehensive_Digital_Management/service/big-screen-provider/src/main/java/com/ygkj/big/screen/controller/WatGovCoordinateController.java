package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WatGovCoordinateControllerApi;
import com.ygkj.big.screen.service.WatGovCoordinateService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/8/18
 */
@RestController
@RequestMapping("watGovCoordinate")
public class WatGovCoordinateController implements WatGovCoordinateControllerApi {

    @Autowired
    WatGovCoordinateService watGovCoordinateService;

    @GetMapping("stats4OaAssets")
    @Override
    public CommonResult stats4OaAssets(@RequestParam("type") int type) {
        return watGovCoordinateService.stats4OaAssets(type);
    }
}
