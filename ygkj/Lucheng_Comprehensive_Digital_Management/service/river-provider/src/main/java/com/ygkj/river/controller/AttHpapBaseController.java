package com.ygkj.river.controller;

import com.ygkj.river.AttHpapBaseControllerApi;
import com.ygkj.river.service.AttHpapBaseService;
import com.ygkj.river.vo.request.AttHpapBaseReqVo;
import com.ygkj.river.vo.response.AttHpapBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘临水线点数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttHpapBase")
public class AttHpapBaseController implements AttHpapBaseControllerApi {

    @Resource
    private AttHpapBaseService attHpapBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttHpapBaseRespVo>> loadPage(AttHpapBaseReqVo vo) {
        return CommonResult.success(attHpapBaseService.loadPage(vo));
    }
}
