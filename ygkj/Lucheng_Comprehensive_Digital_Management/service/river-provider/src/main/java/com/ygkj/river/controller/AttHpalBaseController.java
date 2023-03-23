package com.ygkj.river.controller;

import com.ygkj.river.AttHpalBaseControllerApi;
import com.ygkj.river.service.AttHpalBaseService;
import com.ygkj.river.vo.request.AttHpalBaseReqVo;
import com.ygkj.river.vo.response.AttHpalBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘临水线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttHpalBase")
public class AttHpalBaseController implements AttHpalBaseControllerApi {

    @Resource
    private AttHpalBaseService attHpalBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttHpalBaseRespVo>> loadPage(AttHpalBaseReqVo vo) {
        return CommonResult.success(attHpalBaseService.loadPage(vo));
    }

}
