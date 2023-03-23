package com.ygkj.river.controller;

import com.ygkj.river.AttRsmlBaseControllerApi;
import com.ygkj.river.service.AttRsmlBaseService;
import com.ygkj.river.vo.request.AttRsmlBaseReqVo;
import com.ygkj.river.vo.response.AttRsmlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库管理范围线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRsmlBase")
public class AttRsmlBaseController implements AttRsmlBaseControllerApi {

    @Resource
    private AttRsmlBaseService attRsmlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRsmlBaseRespVo>> loadPage(AttRsmlBaseReqVo vo) {
        return CommonResult.success(attRsmlBaseService.loadPage(vo));
    }
}
