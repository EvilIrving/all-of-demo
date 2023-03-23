package com.ygkj.river.controller;

import com.ygkj.river.AttOwapBaseControllerApi;
import com.ygkj.river.service.AttOwapBaseService;
import com.ygkj.river.vo.request.AttOwapBaseReqVo;
import com.ygkj.river.vo.response.AttOwapBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域临水线点数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttOwapBase")
public class AttOwapBaseController implements AttOwapBaseControllerApi {

    @Resource
    private AttOwapBaseService attOwapBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttOwapBaseRespVo>> loadPage(AttOwapBaseReqVo vo) {
        return CommonResult.success(attOwapBaseService.loadPage(vo));
    }
}
