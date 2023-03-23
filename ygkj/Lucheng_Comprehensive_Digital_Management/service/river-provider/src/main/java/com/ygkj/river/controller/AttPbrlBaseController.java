package com.ygkj.river.controller;

import com.ygkj.river.AttPbrlBaseControllerApi;
import com.ygkj.river.service.AttPbrlBaseService;
import com.ygkj.river.vo.request.AttPbrlBaseReqVo;
import com.ygkj.river.vo.response.AttPbrlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-桥梁数据表
 * @date 2021-07-07
 */
@RestController
@RequestMapping(value = "/AttPbrlBase")
public class AttPbrlBaseController implements AttPbrlBaseControllerApi {

    @Resource
    private AttPbrlBaseService attPbrlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttPbrlBaseRespVo>> loadPage(AttPbrlBaseReqVo vo) {
        return CommonResult.success(attPbrlBaseService.loadPage(vo));
    }
}
