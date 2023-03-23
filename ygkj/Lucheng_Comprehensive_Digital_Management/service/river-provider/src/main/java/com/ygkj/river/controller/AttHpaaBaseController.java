package com.ygkj.river.controller;

import com.ygkj.river.AttHpaaBaseControllerApi;
import com.ygkj.river.service.AttHpaaBaseService;
import com.ygkj.river.vo.request.AttHpaaBaseReqVo;
import com.ygkj.river.vo.response.AttHpaaBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘水域面数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttHpaaBase")
public class AttHpaaBaseController implements AttHpaaBaseControllerApi {

    @Resource
    private AttHpaaBaseService attHpaaBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttHpaaBaseRespVo>> loadPage(AttHpaaBaseReqVo vo) {
        return CommonResult.success(attHpaaBaseService.loadPage(vo));
    }
}
