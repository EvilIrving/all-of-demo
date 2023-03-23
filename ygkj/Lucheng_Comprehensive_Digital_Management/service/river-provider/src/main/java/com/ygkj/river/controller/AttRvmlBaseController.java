package com.ygkj.river.controller;

import com.ygkj.river.AttRvmlBaseControllerApi;
import com.ygkj.river.service.AttRvmlBaseService;
import com.ygkj.river.vo.request.AttRvmlBaseReqVo;
import com.ygkj.river.vo.response.AttRvmlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道管理范围线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRvmlBase")
public class AttRvmlBaseController implements AttRvmlBaseControllerApi {

    @Resource
    private AttRvmlBaseService attRvmlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRvmlBaseRespVo>> loadPage(AttRvmlBaseReqVo vo) {
        return CommonResult.success(attRvmlBaseService.loadPage(vo));
    }
}
