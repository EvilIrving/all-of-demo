package com.ygkj.gragh.controller;


import com.ygkj.gragh.DisasterPreventionControllerApi;
import com.ygkj.gragh.service.DisasterPreventionService;
import com.ygkj.gragh.vo.request.TyphoonReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("disasterPrevention")
public class DisasterPreventionController implements DisasterPreventionControllerApi {

    @Resource
    private DisasterPreventionService disasterPreventionService;

    @Override
    @GetMapping("realTimeTyphoon")
    public CommonResult realTimeTyphoon() {
        return CommonResult.success(disasterPreventionService.realTimeTyphoon());
    }

    @Override
    @GetMapping("selectTyphoon/{id}")
    public CommonResult selectTyphoon(@PathVariable Integer id) {
        return CommonResult.success(disasterPreventionService.selectTyphoon(id));
    }

    @Override
    @PostMapping("historyTyphoon")
    public CommonResult historyTyphoon(TyphoonReqVo typhoonReqVo) {
        return CommonResult.success(disasterPreventionService.historyTyphoon(typhoonReqVo));
    }
}
