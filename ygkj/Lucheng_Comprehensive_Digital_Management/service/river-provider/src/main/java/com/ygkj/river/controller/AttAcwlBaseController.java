package com.ygkj.river.controller;


import com.ygkj.river.AttAcwlBaseControllerApi;
import com.ygkj.river.service.AttAcwlBaseService;
import com.ygkj.river.vo.request.AttAcwlBaseReqVo;
import com.ygkj.river.vo.response.AttAcwlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道水面线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttAcwlBase")
public class AttAcwlBaseController implements AttAcwlBaseControllerApi {

    @Resource
    private AttAcwlBaseService attAcwlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttAcwlBaseRespVo>> loadPage(AttAcwlBaseReqVo vo) {
        return CommonResult.success(attAcwlBaseService.loadPage(vo));
    }
}
