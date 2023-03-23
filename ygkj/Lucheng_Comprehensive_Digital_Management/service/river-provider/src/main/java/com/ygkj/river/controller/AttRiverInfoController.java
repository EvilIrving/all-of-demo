package com.ygkj.river.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.river.model.AttRiverInfo;
import com.ygkj.river.service.AttRiverInfoService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wgf
 * @date 2022/5/25 11:25
 * @Description:
 */
@RestController
@RequestMapping("attRiverInfo")
public class AttRiverInfoController {

    @Autowired
    private AttRiverInfoService attRiverInfoService;

    @PostMapping("/list")
    public CommonResult listPage(AttRiverInfo attRiverInfo) {
        attRiverInfo.page();
        QueryWrapper<AttRiverInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(attRiverInfo.getName()), "NAME", attRiverInfo.getName());
        queryWrapper.eq(StringUtils.isNotBlank(attRiverInfo.getType()), "type", attRiverInfo.getType());
        List<AttRiverInfo> list = attRiverInfoService.list(queryWrapper);
        return CommonResult.success(PageUtils.getPageVO(list));
    }


    @PostMapping("/countType")
    public CommonResult countType() {
        QueryWrapper<AttRiverInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("type");
        queryWrapper.select("type, count(1) count");
        List<Map<String, Object>> list = attRiverInfoService.listMaps(queryWrapper);
        return CommonResult.success(list);
    }
}
