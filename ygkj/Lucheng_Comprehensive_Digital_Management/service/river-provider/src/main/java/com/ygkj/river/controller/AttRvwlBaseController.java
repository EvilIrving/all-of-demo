package com.ygkj.river.controller;

import com.ygkj.river.AttRvwlBaseControllerApi;
import com.ygkj.river.service.AttRvwlBaseService;
import com.ygkj.river.vo.request.AttRvwlBaseReqVo;
import com.ygkj.river.vo.response.AttRvwlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道水面线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRvwlBase")
public class AttRvwlBaseController implements AttRvwlBaseControllerApi {

    @Resource
    private AttRvwlBaseService attRvwlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRvwlBaseRespVo>> loadPage(AttRvwlBaseReqVo vo) {
        return CommonResult.success(attRvwlBaseService.loadPage(vo));
    }
}
