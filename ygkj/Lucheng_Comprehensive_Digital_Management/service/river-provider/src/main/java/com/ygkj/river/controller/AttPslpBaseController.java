package com.ygkj.river.controller;

import com.ygkj.river.AttPslpBaseControllerApi;
import com.ygkj.river.service.AttPslpBaseService;
import com.ygkj.river.vo.request.AttPslpBaseReqVo;
import com.ygkj.river.vo.response.AttPslpBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水闸数据表
 * @date 2021-07-07
 */
@RestController
@RequestMapping(value = "/AttPslpBase")
public class AttPslpBaseController implements AttPslpBaseControllerApi {

    @Resource
    private AttPslpBaseService attPslpBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttPslpBaseRespVo>> loadPage(AttPslpBaseReqVo vo) {
        return CommonResult.success(attPslpBaseService.loadPage(vo));
    }
}
