package com.ygkj.water.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.SectionConfiguration;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.aop.annotation.LogAnnotation;
import com.ygkj.water.project.mapper.SectionConfigurationMapper;
import com.ygkj.water.project.service.SectionConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wgf
 * @date 2022/5/10 17:38
 * @Description:
 */
@Api(tags = "断面配置")
@RestController
@RequestMapping("sectionConfiguration")
public class SectionConfigurationController {

    @Autowired
    private SectionConfigurationService sectionConfigurationService;
    @Resource
    private SectionConfigurationMapper sectionConfigurationMapper;

    @ApiOperation("断面配置新增或修改")
    @PostMapping("/saveOrUpdate")
    @LogAnnotation(title = "断面配置管理",action = "新增或修改断面")
    public CommonResult saveOrUpdate(SectionConfiguration sectionConfiguration) {
        String id = sectionConfiguration.getId();
        if (StringUtils.isBlank(id)){
            if (sectionConfigurationService.save(sectionConfiguration)){
                return CommonResult.success();
            }
        } else {
            if (sectionConfigurationService.updateById(sectionConfiguration)){
                return CommonResult.success();
            }
        }
        return CommonResult.failed("新增或修改失败");
    }

    @ApiOperation("断面列表查询")
    @PostMapping("/list")
    @LogAnnotation(title = "断面配置管理",action = "断面列表查询")
    public CommonResult list(SectionConfiguration sectionConfiguration) {
        QueryWrapper<SectionConfiguration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(sectionConfiguration.getId()), "id", sectionConfiguration.getId());
        queryWrapper.eq(StringUtils.isNotBlank(sectionConfiguration.getPrcd()), "prcd", sectionConfiguration.getPrcd());
        queryWrapper.like(StringUtils.isNotBlank(sectionConfiguration.getName()), "name", sectionConfiguration.getName());
        queryWrapper.orderByDesc("creation_time");
        sectionConfiguration.page();
        List<SectionConfiguration> list = sectionConfigurationService.list(queryWrapper);
        JSONObject jsonObject = sectionConfigurationService.automaticAcquisition();
        //插入实时查询的下一小时风速和天文潮数据
        for (SectionConfiguration configuration : list) {
            //插入主测站
            SectionConfiguration srcdById = sectionConfigurationMapper.selectSrcdById(configuration.getId());
            Double speed = jsonObject.getDoubleValue("speed");
            Double astrotd = jsonObject.getDoubleValue("astrotd");
            configuration.setSpeed(speed);
            configuration.setAstrotd(astrotd);
            configuration.setStcd(srcdById!=null?srcdById.getStcd():"");
        }
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @ApiOperation("断面配置删除")
    @GetMapping("/del")
    @LogAnnotation(title = "断面配置管理",action = "删除断面")
    public CommonResult del(String id) {
       if (sectionConfigurationService.removeById(id)){
           return CommonResult.success();
       }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("单独查询鹿城区下一小时天文潮数据，风速")


    @PostMapping("/automaticAcquisition")
    public CommonResult automaticAcquisition() {
        return CommonResult.success(sectionConfigurationService.automaticAcquisition());
    }

    @ApiOperation("大屏断面打点弹窗")
    @PostMapping("/windows")
    public CommonResult windows(String id) {
        return CommonResult.success(sectionConfigurationService.windows(id));
    }

    @ApiOperation("预测断面列表")
    @PostMapping("/forecastSectionPage")
    public CommonResult forecastSectionPage(SectionConfiguration sectionConfiguration) {
        CommonResult list = list(sectionConfiguration);
        PageVO<SectionConfiguration> pageVO = (PageVO<SectionConfiguration>)list.getData();
        List<SectionConfiguration> data = pageVO.getList();
        List<SectionConfiguration> configurationList = sectionConfigurationService.forecastSectionPage(data);
        pageVO.setList(configurationList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation("预测断面详情")
    @GetMapping("/forecastSectionInfo")
    public CommonResult forecastSectionInfo(String sectionId) {
        return CommonResult.success(sectionConfigurationService.forecastSectionInfo(sectionId));
    }

    @ApiOperation("预测断面24小时预报高潮位")
    @GetMapping("/forecastMaxHour")
    public CommonResult forecastMaxHour(SectionConfiguration sectionConfiguration) {
        return CommonResult.success(sectionConfigurationService.forecastMaxHour(sectionConfiguration));
    }

    @ApiOperation("预测断面48小时实时与预报数据")
    @GetMapping("/forecastSectionHour")
    public CommonResult forecastSectionHour(String sectionId) {
        return CommonResult.success(sectionConfigurationService.forecastSectionHour(sectionId));
    }
}
