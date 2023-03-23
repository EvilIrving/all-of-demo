package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.RiskDetailsHiddendangerControllerApi;
import com.ygkj.big.screen.service.RiskDetailsHiddendangerService;
import com.ygkj.big.screen.vo.response.RiskDetailsHiddenDangerResp;
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
 * @date 2021/6/16 16:58
 */
@RestController
@RequestMapping("riskDetailsHiddendanger")
public class RiskDetailsHiddendangerController implements RiskDetailsHiddendangerControllerApi {

    @Autowired
    private RiskDetailsHiddendangerService riskDetailsHiddendangerService;

    @PostMapping("cancellationHiddenDangerStatic")
    @Override
    public CommonResult<List<RiskHiddenDangerStaticResp>> cancellationHiddenDangerStatic() {
        return CommonResult.success(riskDetailsHiddendangerService.cancellationHiddenDangerStatic());
    }

    @PostMapping("findDetailsHiddenDangerBySbId")
    @Override
    public CommonResult<RiskDetailsHiddenDangerResp> findDetailsHiddenDangerBySbId(String sbId) {
        return CommonResult.success(riskDetailsHiddendangerService.findDetailsHiddenDangerBySbId(sbId));
    }
}
