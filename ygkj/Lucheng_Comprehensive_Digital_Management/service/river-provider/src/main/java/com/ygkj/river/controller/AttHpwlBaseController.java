package com.ygkj.river.controller;

import com.ygkj.river.AttHpwlBaseControllerApi;
import com.ygkj.river.service.AttHpwlBaseService;
import com.ygkj.river.vo.request.AttHpwlBaseReqVo;
import com.ygkj.river.vo.response.AttHpwlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘水面线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttHpwlBase")
public class AttHpwlBaseController implements AttHpwlBaseControllerApi {

    @Resource
    private AttHpwlBaseService attHpwlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttHpwlBaseRespVo>> loadPage(AttHpwlBaseReqVo vo) {
        return CommonResult.success(attHpwlBaseService.loadPage(vo));
    }
}
