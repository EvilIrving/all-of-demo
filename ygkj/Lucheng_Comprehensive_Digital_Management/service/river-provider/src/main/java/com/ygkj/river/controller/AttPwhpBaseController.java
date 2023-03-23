package com.ygkj.river.controller;

import com.ygkj.river.AttPwhpBaseControllerApi;
import com.ygkj.river.service.AttPwhpBaseService;
import com.ygkj.river.vo.request.AttPwhpBaseReqVo;
import com.ygkj.river.vo.response.AttPwhpBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-码头数据表
 * @date 2021-07-07
 */
@RestController
@RequestMapping(value = "/AttPwhpBase")
public class AttPwhpBaseController implements AttPwhpBaseControllerApi {

    @Resource
    private AttPwhpBaseService attPwhpBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttPwhpBaseRespVo>> loadPage(AttPwhpBaseReqVo vo) {
        return CommonResult.success(attPwhpBaseService.loadPage(vo));
    }
}
