package com.ygkj.river.controller;

import com.ygkj.river.AttRvolBaseControllerApi;
import com.ygkj.river.service.AttRvolBaseService;
import com.ygkj.river.vo.request.AttRvolBaseReqVo;
import com.ygkj.river.vo.response.AttRvolBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他沟渠数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRvolBase")
public class AttRvolBaseController implements AttRvolBaseControllerApi {

    @Resource
    private AttRvolBaseService attRvolBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRvolBaseRespVo>> loadPage(AttRvolBaseReqVo vo) {
        return CommonResult.success(attRvolBaseService.loadPage(vo));
    }
}
