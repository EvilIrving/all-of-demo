package com.ygkj.river.controller;

import com.ygkj.river.AttAcclBaseControllerApi;
import com.ygkj.river.service.AttAcclBaseService;
import com.ygkj.river.vo.request.AttAcclBaseReqVo;
import com.ygkj.river.vo.response.AttAcclBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道中心线数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttAcclBase")
public class AttAcclBaseController implements AttAcclBaseControllerApi {

    @Resource
    private AttAcclBaseService attAcclBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttAcclBaseRespVo>> loadPage(AttAcclBaseReqVo vo) {
        return CommonResult.success(attAcclBaseService.loadPage(vo));
    }
}
