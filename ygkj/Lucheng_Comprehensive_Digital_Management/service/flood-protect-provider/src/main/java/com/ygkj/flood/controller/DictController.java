package com.ygkj.flood.controller;

import com.ygkj.flood.DictControllerApi;
import com.ygkj.flood.service.AttDictService;
import com.ygkj.flood.vo.request.AttDictAddVo;
import com.ygkj.flood.vo.request.AttDictRequestVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict")
public class DictController implements DictControllerApi {

    @Autowired
    private AttDictService attDictService;

    @PostMapping("/dicts")
    @Override
    public CommonResult selectDict(AttDictRequestVo requestVo) {
        return CommonResult.success(attDictService.selectDict(requestVo));
    }

    @PostMapping("/insertDict")
    @Override
    public CommonResult insertDict(AttDictAddVo attDictAddVo) {
        if (attDictService.insert(attDictAddVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }
}
