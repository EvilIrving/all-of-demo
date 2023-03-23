package com.ygkj.river.controller;

import com.ygkj.river.AttRvclBaseControllerApi;
import com.ygkj.river.service.AttRvclBaseService;
import com.ygkj.river.vo.request.AttRvclBaseReqVo;
import com.ygkj.river.vo.response.AttRvclBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道中心线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRvclBase")
public class AttRvclBaseController implements AttRvclBaseControllerApi {

    @Resource
    private AttRvclBaseService attRvclBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRvclBaseRespVo>> loadPage(AttRvclBaseReqVo vo) {
        return CommonResult.success(attRvclBaseService.loadPage(vo));
    }
}
