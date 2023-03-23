package com.ygkj.river.controller;

import com.ygkj.river.AttPdmpBaseControllerApi;
import com.ygkj.river.service.AttPdmpBaseService;
import com.ygkj.river.vo.request.AttPdmpBaseReqVo;
import com.ygkj.river.vo.response.AttPdmpBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-拦水坝（堰）数据表
 * @date 2021-07-07
 */
@RestController
@RequestMapping(value = "/AttPdmpBase")
public class AttPdmpBaseController implements AttPdmpBaseControllerApi {

    @Resource
    private AttPdmpBaseService attPdmpBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttPdmpBaseRespVo>> loadPage(AttPdmpBaseReqVo vo) {
        return CommonResult.success(attPdmpBaseService.loadPage(vo));
    }
}
