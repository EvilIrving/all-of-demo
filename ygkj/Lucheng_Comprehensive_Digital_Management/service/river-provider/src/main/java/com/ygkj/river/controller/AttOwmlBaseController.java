package com.ygkj.river.controller;

import com.ygkj.river.AttOwmlBaseControllerApi;
import com.ygkj.river.service.AttOwmlBaseService;
import com.ygkj.river.vo.request.AttOwmlBaseReqVo;
import com.ygkj.river.vo.response.AttOwmlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域管理范围线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttOwmlBase")
public class AttOwmlBaseController implements AttOwmlBaseControllerApi {

    @Resource
    private AttOwmlBaseService attOwmlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttOwmlBaseRespVo>> loadPage(AttOwmlBaseReqVo vo) {
        return CommonResult.success(attOwmlBaseService.loadPage(vo));
    }
}
