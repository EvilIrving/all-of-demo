package com.ygkj.river.controller;

import com.ygkj.river.AttOwwlBaseControllerApi;
import com.ygkj.river.service.AttOwwlBaseService;
import com.ygkj.river.vo.request.AttOwwlBaseReqVo;
import com.ygkj.river.vo.response.AttOwwlBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域水面线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttOwwlBase")
public class AttOwwlBaseController implements AttOwwlBaseControllerApi {

    @Resource
    private AttOwwlBaseService attOwwlBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttOwwlBaseRespVo>> loadPage(AttOwwlBaseReqVo vo) {
        return CommonResult.success(attOwwlBaseService.loadPage(vo));
    }
}
