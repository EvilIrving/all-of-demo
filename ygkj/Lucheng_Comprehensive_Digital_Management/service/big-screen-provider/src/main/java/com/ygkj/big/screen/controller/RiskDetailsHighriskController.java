package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.RiskDetailsHighriskControllerApi;
import com.ygkj.big.screen.service.RiskDetailsHighriskService;
import com.ygkj.big.screen.vo.response.RiskDetailsHighriskResp;
import com.ygkj.big.screen.vo.response.RiskHiddenDangerStaticResp;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/17 10:14
 */
@RestController
@RequestMapping("/riskDetailsHighrisk")
public class RiskDetailsHighriskController implements RiskDetailsHighriskControllerApi {

    @Autowired
    private RiskDetailsHighriskService riskDetailsHighriskService;


    @PostMapping("findDetailsHighriskBySbId")
    @Override
    public CommonResult<RiskDetailsHighriskResp> findDetailsHighriskBySbId(String sbId) {
        return CommonResult.success(riskDetailsHighriskService.findDetailsHighriskBySbId(sbId));
    }

    @PostMapping("cancellationWeakPointsStatic")
    @Override
    public CommonResult<List<RiskHiddenDangerStaticResp>> cancellationWeakPointsStatic() {
        return CommonResult.success(riskDetailsHighriskService.cancellationWeakPointsStatic());
    }
}
