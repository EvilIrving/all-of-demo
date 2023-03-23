package com.ygkj.river.controller;

import com.ygkj.river.AttRsaaBaseControllerApi;
import com.ygkj.river.service.AttRsaaBaseService;
import com.ygkj.river.vo.request.AttRsaaBaseReqVo;
import com.ygkj.river.vo.response.AttRsaaBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库水域面数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRsaaBase")
public class AttRsaaBaseController implements AttRsaaBaseControllerApi {

    @Resource
    private AttRsaaBaseService attRsaaBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRsaaBaseRespVo>> loadPage(AttRsaaBaseReqVo vo) {
        return CommonResult.success(attRsaaBaseService.loadPage(vo));
    }
}
