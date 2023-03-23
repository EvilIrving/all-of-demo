package com.ygkj.river.controller;

import com.ygkj.river.AttSmallMediumRiverControllerApi;
import com.ygkj.river.service.AttSmallMediumRiverService;
import com.ygkj.river.vo.request.AttSmallMediumRiverReqVo;
import com.ygkj.river.vo.response.AttSmallMediumRiverRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/AttSmallMediumRiver")
public class AttSmallMediumRiverController implements AttSmallMediumRiverControllerApi {

    @Resource
    private AttSmallMediumRiverService attSmallMediumRiverService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttSmallMediumRiverRespVo>> loadPage(AttSmallMediumRiverReqVo vo) {
        return CommonResult.success(attSmallMediumRiverService.loadPage(vo));
    }
}
