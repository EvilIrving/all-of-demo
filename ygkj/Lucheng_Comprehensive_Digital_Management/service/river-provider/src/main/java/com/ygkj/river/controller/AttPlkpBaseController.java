package com.ygkj.river.controller;

import com.ygkj.river.AttPlkpBaseControllerApi;
import com.ygkj.river.service.AttPlkpBaseService;
import com.ygkj.river.vo.request.AttPlkpBaseReqVo;
import com.ygkj.river.vo.response.AttPlkpBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-船闸数据表
 * @date 2021-07-07
 */
@RestController
@RequestMapping(value = "/AttPlkpBase")
public class AttPlkpBaseController implements AttPlkpBaseControllerApi {

    @Resource
    private AttPlkpBaseService attPlkpBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttPlkpBaseRespVo>> loadPage(AttPlkpBaseReqVo vo) {
        return CommonResult.success(attPlkpBaseService.loadPage(vo));
    }
}
