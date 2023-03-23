package com.ygkj.river.controller;

import com.ygkj.river.AttAcapBaseControllerApi;
import com.ygkj.river.service.AttAcapBaseService;
import com.ygkj.river.vo.request.AttAcapBaseReqVo;
import com.ygkj.river.vo.response.AttAcapBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道临水线点数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttAcapBase")
public class AttAcapBaseController implements AttAcapBaseControllerApi {

    @Resource
    private AttAcapBaseService attAcapBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttAcapBaseRespVo>> loadPage(AttAcapBaseReqVo vo) {
        return CommonResult.success(attAcapBaseService.loadPage(vo));
    }
}
