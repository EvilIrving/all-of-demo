package com.ygkj.river.controller;

import com.ygkj.river.AttRsalBaseControllerApi;
import com.ygkj.river.service.AttRsalBaseService;
import com.ygkj.river.vo.request.AttRsalBaseReqVo;
import com.ygkj.river.vo.response.AttRsalBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库临水线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRsalBase")
public class AttRsalBaseController implements AttRsalBaseControllerApi {

    @Resource
    private AttRsalBaseService attRsalBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRsalBaseRespVo>> loadPage(AttRsalBaseReqVo vo) {
        return CommonResult.success(attRsalBaseService.loadPage(vo));
    }
}
