package com.ygkj.river.controller;

import com.ygkj.entity.SingletonMap;
import com.ygkj.river.AttRivInletControllerApi;
import com.ygkj.river.model.AttRivInletBase;
import com.ygkj.river.service.RivInletService;
import com.ygkj.river.vo.request.RivInletReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/6
 */
@RestController
@RequestMapping("rivInlet")
public class AttRivInletController implements AttRivInletControllerApi {

    @Autowired
    RivInletService rivInletService;

    @GetMapping("list")
    @Override
    public CommonResult<List<AttRivInletBase>> list(RivInletReqVo reqVo) {
        return CommonResult.success(rivInletService.list(reqVo));
    }

    @GetMapping("rivInletStats")
    @Override
    public CommonResult<List<SingletonMap<String, Long>>> rivInletStats(Integer statType, Integer recordType) {
        return CommonResult.success(rivInletService.rivInletStats(statType, recordType));
    }
}
