package com.ygkj.river.controller;

import com.ygkj.river.AttPpspBaseControllerApi;
import com.ygkj.river.service.AttPpspBaseService;
import com.ygkj.river.vo.request.AttPpspBaseReqVo;
import com.ygkj.river.vo.response.AttPpspBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-泵站数据表
 * @date 2021-07-07
 */
@RestController
@RequestMapping(value = "/AttPpspBase")
public class AttPpspBaseController implements AttPpspBaseControllerApi {

    @Resource
    private AttPpspBaseService attPpspBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttPpspBaseRespVo>> loadPage(AttPpspBaseReqVo vo) {
        return CommonResult.success(attPpspBaseService.loadPage(vo));
    }
}
