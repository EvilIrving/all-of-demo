package com.ygkj.river.controller;

import com.ygkj.river.AttOwaaBaseControllerApi;
import com.ygkj.river.service.AttOwaaBaseService;
import com.ygkj.river.vo.request.AttOwaaBaseReqVo;
import com.ygkj.river.vo.response.AttOwaaBaseRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域水域面数据表
 * @date 2021-07-06
 */
@RestController
@RequestMapping(value = "/AttOwaaBase")
public class AttOwaaBaseController implements AttOwaaBaseControllerApi {

    @Resource
    private AttOwaaBaseService attOwaaBaseService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttOwaaBaseRespVo>> loadPage(AttOwaaBaseReqVo vo) {
        return CommonResult.success(attOwaaBaseService.loadPage(vo));
    }
}
