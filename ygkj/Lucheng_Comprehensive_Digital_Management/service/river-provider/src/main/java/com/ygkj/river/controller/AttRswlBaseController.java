package com.ygkj.river.controller;

import com.ygkj.river.AttRswlBaseControllerApi;
import com.ygkj.river.service.AttRswlBaseService;
import com.ygkj.river.vo.request.AttRswlBaseReqVo;
import com.ygkj.river.vo.response.AttRswlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库水面线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttRswlBase")
public class AttRswlBaseController implements AttRswlBaseControllerApi {

    @Resource
    private AttRswlBaseService attRswlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttRswlBaseRespVo>> loadPage(AttRswlBaseReqVo vo) {
        return CommonResult.success(attRswlBaseService.loadPage(vo));
    }
}
