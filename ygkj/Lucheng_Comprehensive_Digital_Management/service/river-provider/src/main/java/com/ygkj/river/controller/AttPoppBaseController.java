package com.ygkj.river.controller;

import com.ygkj.river.AttPoppBaseControllerApi;
import com.ygkj.river.service.AttPoppBaseService;
import com.ygkj.river.vo.request.AttPoppBaseReqVo;
import com.ygkj.river.vo.response.AttPoppBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他工程数据表
 * @date 2021-07-07
 */
@RestController
@RequestMapping(value = "/AttPoppBase")
public class AttPoppBaseController implements AttPoppBaseControllerApi {

    @Resource
    private AttPoppBaseService attPoppBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttPoppBaseRespVo>> loadPage(AttPoppBaseReqVo vo) {
        return CommonResult.success(attPoppBaseService.loadPage(vo));
    }
}
