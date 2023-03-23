package com.ygkj.river.controller;

import com.ygkj.river.AttRvapBaseControllerApi;
import com.ygkj.river.service.AttRvapBaseService;
import com.ygkj.river.vo.request.AttRvapBaseReqVo;
import com.ygkj.river.vo.response.AttRvapBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道临水线点数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRvapBase")
public class AttRvapBaseController implements AttRvapBaseControllerApi {

    @Resource
    private AttRvapBaseService attRvapBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRvapBaseRespVo>> loadPage(AttRvapBaseReqVo vo) {
        return CommonResult.success(attRvapBaseService.loadPage(vo));
    }
}
