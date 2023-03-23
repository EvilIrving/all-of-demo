package com.ygkj.river.controller;

import com.ygkj.river.AttPdklBaseControllerApi;
import com.ygkj.river.service.AttPdklBaseService;
import com.ygkj.river.vo.request.AttPdklBaseReqVo;
import com.ygkj.river.vo.response.AttPdklBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-堤防数据表
 * @date 2021-07-07
 */
@RestController
@RequestMapping(value = "/AttPdklBase")
public class AttPdklBaseController implements AttPdklBaseControllerApi {

    @Resource
    private AttPdklBaseService attPdklBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttPdklBaseRespVo>> loadPage(AttPdklBaseReqVo vo) {
        return CommonResult.success(attPdklBaseService.loadPage(vo));
    }
}
