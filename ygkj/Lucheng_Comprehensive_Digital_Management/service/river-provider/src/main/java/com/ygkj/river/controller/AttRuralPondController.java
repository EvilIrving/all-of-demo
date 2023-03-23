package com.ygkj.river.controller;

import com.ygkj.river.AttRuralPondControllerApi;
import com.ygkj.river.service.AttRuralPondService;
import com.ygkj.river.vo.request.AttRuralPondReqVo;
import com.ygkj.river.vo.response.AttRuralPondRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 农村池塘表
 * @date 2021-08-12
 */
@RestController
@RequestMapping(value = "/AttRuralPond")
public class AttRuralPondController implements AttRuralPondControllerApi {

    @Resource
    private AttRuralPondService attRuralPondService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRuralPondRespVo>> loadPage(AttRuralPondReqVo vo) {
        return CommonResult.success(attRuralPondService.loadPage(vo));
    }
}
