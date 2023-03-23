package com.ygkj.river.controller;

import com.ygkj.river.AttBeautVillageControllerApi;
import com.ygkj.river.service.AttBeautVillageService;
import com.ygkj.river.vo.request.AttBeautVillageReqVo;
import com.ygkj.river.vo.response.AttBeautVillageRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水美乡镇表
 * @date 2021-08-12
 */
@RestController
@RequestMapping(value = "/AttBeautVillage")
public class AttBeautVillageController implements AttBeautVillageControllerApi {

    @Resource
    private AttBeautVillageService attBeautVillageService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttBeautVillageRespVo>> loadPage(AttBeautVillageReqVo vo) {
        return CommonResult.success(attBeautVillageService.loadPage(vo));
    }
}
