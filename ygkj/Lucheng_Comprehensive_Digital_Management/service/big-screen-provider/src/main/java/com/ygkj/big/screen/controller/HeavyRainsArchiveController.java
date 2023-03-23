package com.ygkj.big.screen.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.big.screen.model.HeavyRainsArchive;
import com.ygkj.big.screen.service.HeavyRainsArchiveService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/6 13:48
 * @Description: 暴雨归档
 */
@Api(tags ="暴雨归档")
@RestController
@RequestMapping("heavyRainsArchive")
public class HeavyRainsArchiveController {

    @Autowired
    private HeavyRainsArchiveService heavyRainsArchiveService;

    @ApiOperation(value = "暴雨归档新增和修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(HeavyRainsArchive heavyRainsArchive) {
        if (StringUtils.isNotBlank(heavyRainsArchive.getId())) {
            //修改
            if (heavyRainsArchiveService.updateById(heavyRainsArchive)) {
                return CommonResult.success();
            }
        } else {
            //新增
            if (heavyRainsArchiveService.save(heavyRainsArchive)) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed("暴雨归档新增或修改失败");
    }

    @ApiOperation(value = "计算时间范围内所有雨量站降雨量总值的平均值")
    @GetMapping("/precipitation")
    public CommonResult precipitation(HeavyRainsArchive heavyRainsArchive) {
        String value = heavyRainsArchiveService.precipitation(heavyRainsArchive);
        return CommonResult.success(value);
    }

    @ApiOperation(value = "详情-查询时间范围内小时雨量值")
    @GetMapping("/hourlyRainfallvalue")
    public CommonResult hourlyRainfallvalue(HeavyRainsArchive heavyRainsArchive) {
        HeavyRainsArchive archive = heavyRainsArchiveService.getById(heavyRainsArchive.getId());
        List<JSONObject> value = heavyRainsArchiveService.hourlyRainfallvalue(archive);
        return CommonResult.success(value);
    }

    @ApiOperation(value = "刪除数据")
    @GetMapping("/del")
    public CommonResult del(String id) {
        if (heavyRainsArchiveService.removeById(id)){
           return CommonResult.success();
        } else {
            return CommonResult.failed("删除失败");
        }
    }

    @ApiOperation(value = "暴雨归档列表")
    @PostMapping("/list")
    public CommonResult listPage(HeavyRainsArchive heavyRainsArchive) {
        heavyRainsArchive.page();
        QueryWrapper<HeavyRainsArchive> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(heavyRainsArchive.getName()), "name", heavyRainsArchive.getName());
        queryWrapper.eq(StringUtils.isNotBlank(heavyRainsArchive.getId()), "id", heavyRainsArchive.getId());
        queryWrapper.orderByDesc("start_time");
        List<HeavyRainsArchive> list = heavyRainsArchiveService.list(queryWrapper);
        return CommonResult.success( PageUtils.getPageVO(list));
    }
}
