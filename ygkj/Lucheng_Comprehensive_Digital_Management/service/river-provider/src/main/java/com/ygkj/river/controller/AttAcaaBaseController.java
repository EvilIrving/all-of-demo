package com.ygkj.river.controller;

import com.ygkj.river.AttAcaaBaseControllerApi;
import com.ygkj.river.service.AttAcaaBaseService;
import com.ygkj.river.vo.request.AttAcaaBaseReqVo;
import com.ygkj.river.vo.response.AttAcaaBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道水域面数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttAcaaBase")
public class AttAcaaBaseController implements AttAcaaBaseControllerApi {

    @Resource
    private AttAcaaBaseService attAcaaBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttAcaaBaseRespVo>> loadPage(AttAcaaBaseReqVo vo) {
        return CommonResult.success(attAcaaBaseService.loadPage(vo));
    }
}
