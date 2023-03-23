package com.ygkj.river.controller;

import com.ygkj.river.RiverOutletControllerApi;
import com.ygkj.river.service.RiverOutletService;
import com.ygkj.river.vo.request.RiverOutletQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/10/9
 */
@RestController
@RequestMapping("riverOutlet")
public class RiverOutletController implements RiverOutletControllerApi {

    @Autowired
    RiverOutletService riverOutletService;

    @GetMapping("pageList")
    @Override
    public CommonResult pageList(RiverOutletQueryVo queryVo) {
        return CommonResult.success(riverOutletService.pageList(queryVo));
    }

    @GetMapping("stats4PageList")
    @Override
    public CommonResult stats4PageList(RiverOutletQueryVo queryVo) {
        return CommonResult.success(riverOutletService.stats4PageList(queryVo));
    }
}
