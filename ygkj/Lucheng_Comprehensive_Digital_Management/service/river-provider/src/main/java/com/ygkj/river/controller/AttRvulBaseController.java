package com.ygkj.river.controller;

import com.ygkj.river.AttRvulBaseControllerApi;
import com.ygkj.river.service.AttRvulBaseService;
import com.ygkj.river.vo.request.AttRvulBaseReqVo;
import com.ygkj.river.vo.response.AttRvulBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-暗河数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRvulBase")
public class AttRvulBaseController implements AttRvulBaseControllerApi {

    @Resource
    private AttRvulBaseService attRvulBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRvulBaseRespVo>> loadPage(AttRvulBaseReqVo vo) {
        return CommonResult.success(attRvulBaseService.loadPage(vo));
    }
}
