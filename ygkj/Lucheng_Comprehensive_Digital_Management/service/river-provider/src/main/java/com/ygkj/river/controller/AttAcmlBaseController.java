package com.ygkj.river.controller;

import com.ygkj.river.AttAcmlBaseControllerApi;
import com.ygkj.river.service.AttAcmlBaseService;
import com.ygkj.river.vo.request.AttAcmlBaseReqVo;
import com.ygkj.river.vo.response.AttAcmlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道管理范围线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttAcmlBase")
public class AttAcmlBaseController implements AttAcmlBaseControllerApi {

    @Resource
    private AttAcmlBaseService attAcmlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttAcmlBaseRespVo>> loadPage(AttAcmlBaseReqVo vo) {
        return CommonResult.success(attAcmlBaseService.loadPage(vo));
    }
}
