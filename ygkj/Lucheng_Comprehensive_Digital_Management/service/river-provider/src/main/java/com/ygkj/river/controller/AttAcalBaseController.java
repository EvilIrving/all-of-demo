package com.ygkj.river.controller;

import com.ygkj.river.AttAcalBaseControllerApi;
import com.ygkj.river.service.AttAcalBaseService;
import com.ygkj.river.vo.request.AttAcalBaseReqVo;
import com.ygkj.river.vo.response.AttAcalBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道临水线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttAcalBase")
public class AttAcalBaseController implements AttAcalBaseControllerApi {

    @Resource
    private AttAcalBaseService attAcalBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttAcalBaseRespVo>> loadPage(AttAcalBaseReqVo vo) {
        return CommonResult.success(attAcalBaseService.loadPage(vo));
    }
}
