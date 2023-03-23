package com.ygkj.river.controller;

import com.ygkj.river.AttRvalBaseControllerApi;
import com.ygkj.river.service.AttRvalBaseService;
import com.ygkj.river.vo.request.AttRvalBaseReqVo;
import com.ygkj.river.vo.response.AttRvalBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道临水线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRvalBase")
public class AttRvalBaseController implements AttRvalBaseControllerApi {

    @Resource
    private AttRvalBaseService attRvalBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRvalBaseRespVo>> loadPage(AttRvalBaseReqVo vo) {
        return CommonResult.success(attRvalBaseService.loadPage(vo));
    }
}
