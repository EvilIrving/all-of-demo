package com.ygkj.river.controller;

import com.ygkj.river.AttOwalBaseControllerApi;
import com.ygkj.river.service.AttOwalBaseService;
import com.ygkj.river.vo.request.AttOwalBaseReqVo;
import com.ygkj.river.vo.response.AttOwalBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域临水线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttOwalBase")
public class AttOwalBaseController implements AttOwalBaseControllerApi {

    @Resource
    private AttOwalBaseService attOwalBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttOwalBaseRespVo>> loadPage(AttOwalBaseReqVo vo) {
        return CommonResult.success(attOwalBaseService.loadPage(vo));
    }
}
