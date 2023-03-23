package com.ygkj.river.controller;

import com.ygkj.river.AttRvaaBaseControllerApi;
import com.ygkj.river.service.AttRvaaBaseService;
import com.ygkj.river.vo.request.AttRvaaBaseReqVo;
import com.ygkj.river.vo.response.AttRvaaBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/AttRvaaBase")
public class AttRvaaBaseController implements AttRvaaBaseControllerApi {

    @Resource
    private AttRvaaBaseService attRvaaBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRvaaBaseRespVo>> loadPage(AttRvaaBaseReqVo vo) {
        return CommonResult.success(attRvaaBaseService.loadPage(vo));
    }
}
