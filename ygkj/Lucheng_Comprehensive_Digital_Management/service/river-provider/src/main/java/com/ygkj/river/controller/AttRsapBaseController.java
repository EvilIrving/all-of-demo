package com.ygkj.river.controller;

import com.ygkj.river.AttRsapBaseControllerApi;
import com.ygkj.river.service.AttRsapBaseService;
import com.ygkj.river.vo.request.AttRsapBaseReqVo;
import com.ygkj.river.vo.response.AttRsapBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库临水线点数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRsapBase")
public class AttRsapBaseController implements AttRsapBaseControllerApi {

    @Resource
    private AttRsapBaseService attRsapBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRsapBaseRespVo>> loadPage(AttRsapBaseReqVo vo) {
        return CommonResult.success(attRsapBaseService.loadPage(vo));
    }
}
