package com.ygkj.river.controller;

import com.ygkj.river.AttHpmlBaseControllerApi;
import com.ygkj.river.service.AttHpmlBaseService;
import com.ygkj.river.vo.request.AttHpmlBaseReqVo;
import com.ygkj.river.vo.response.AttHpmlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘管理范围线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttHpmlBase")
public class AttHpmlBaseController implements AttHpmlBaseControllerApi {

    @Resource
    private AttHpmlBaseService attHpmlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttHpmlBaseRespVo>> loadPage(AttHpmlBaseReqVo vo) {
        return CommonResult.success(attHpmlBaseService.loadPage(vo));
    }
}
