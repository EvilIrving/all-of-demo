package com.ygkj.big.screen.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.model.AttStBase;
import com.ygkj.big.screen.model.SectionInformation;
import com.ygkj.big.screen.model.SectionModelData;
import com.ygkj.big.screen.service.SectionInformationService;
import com.ygkj.big.screen.service.SectionModelDataService;
import com.ygkj.big.screen.vo.request.SynWaterRequestVo;
import com.ygkj.enums.StationType;
import com.ygkj.utils.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2022/5/19 16:40
 * @Description: 断面信息表
 */
@Api(tags = "断面信息表")
@RestController
@RequestMapping("sectionInformation")
public class SectionInformationController {
    @Autowired
    private SectionInformationService sectionInformationService;
    @Autowired
    private SectionModelDataService sectionModelDataService;

    @Autowired
    private GraphClient graphClient;


    @ApiOperation("断面信息表新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(SectionInformation sectionInformation) {
        String id = sectionInformation.getId();
        if (StringUtils.isBlank(id)){
            if (sectionInformationService.save(sectionInformation)){
                return CommonResult.success();
            }
        } else {
            if (sectionInformationService.updateById(sectionInformation)){
                return CommonResult.success();
            }
        }
        return CommonResult.failed("新增或修改失败");
    }

    @ApiOperation("断面信息表列表查询")
    @PostMapping("/list")
    public CommonResult list(SectionInformation sectionInformation) {
        QueryWrapper<SectionInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(sectionInformation.getId()), "id", sectionInformation.getId());
        queryWrapper.like(StringUtils.isNotBlank(sectionInformation.getName()), "name", sectionInformation.getName());
        queryWrapper.orderByDesc("creation_time");
        sectionInformation.page();
        List<SectionInformation> list = sectionInformationService.list(queryWrapper);
        list = sectionInformationService.fillData(list);
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @ApiOperation("断面信息表删除")
    @GetMapping("/del")
    public CommonResult del(String id) {
        if (sectionInformationService.removeById(id)){
            return CommonResult.success();
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation("戌浦江断面水位图数据")
    @PostMapping("/hydrograph")
    public CommonResult hydrograph(String time,String startTime,String endTime) {
        return CommonResult.success(sectionInformationService.hydrograph(time,startTime,endTime));
    }

    @ApiOperation("实时预警")
    @GetMapping("/overAlert")
    public CommonResult overAlert(){
        List<JSONObject> result = new ArrayList<>();
        QueryWrapper<SectionInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("stcd");
        List<SectionInformation> list = sectionInformationService.list(queryWrapper);
        List<String> stcds = list.stream().map(SectionInformation::getStcd).collect(Collectors.toList());

        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.TIDE.type()).with(SynWaterRequestVo::setStCodes,stcds.stream().collect(Collectors.joining(","))).build());
        CommonResult<List<AttStBase>> riverRes = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.RIVER.type()).with(SynWaterRequestVo::setStCodes,stcds.stream().collect(Collectors.joining(","))).build());
        List<AttStBase> riData = riverRes.getData();
        List<AttStBase> data = response.getData();
        data.addAll(riData);
        for (SectionInformation sectionInformation : list) {
            JSONObject json = new JSONObject();
            List<AttStBase> collect = data.stream().filter(d -> d.getStCode().equals(sectionInformation.getStcd())).collect(Collectors.toList());
            json.put("sectionName",sectionInformation.getName());
            if (CollectionUtils.isNotBlank(collect)){
                json.put("waterLevel",collect.get(0).getWaterLevel());
                json.put("warningLevel",collect.get(0).getWarningLevel());
                json.put("guaranteeLevel",collect.get(0).getGuaranteeLevel());
                json.put("updateTime",collect.get(0).getTime());
            }else {
                json.put("waterLevel","-");
                json.put("warningLevel","-");
                json.put("guaranteeLevel","-");
                json.put("updateTime","-");
            }
            result.add(json);
        }
        return CommonResult.success(result);
    }

    @ApiOperation("预报预警")
    @GetMapping("/forecastAlert")
    public CommonResult forecastAlert(){
        List<JSONObject> result = new ArrayList<>();
        QueryWrapper<SectionInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("stcd");
        List<SectionInformation> list = sectionInformationService.list(queryWrapper);
        List<String> stcds = list.stream().map(SectionInformation::getStcd).collect(Collectors.toList());

        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.TIDE.type()).with(SynWaterRequestVo::setStCodes,stcds.stream().collect(Collectors.joining(","))).build());
        CommonResult<List<AttStBase>> riverRes = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.RIVER.type()).with(SynWaterRequestVo::setStCodes,stcds.stream().collect(Collectors.joining(","))).build());
        List<AttStBase> riData = riverRes.getData();
        List<AttStBase> data = response.getData();
        data.addAll(riData);

        //预报水位
        QueryWrapper<SectionModelData> modelWrapper = new QueryWrapper<>();
        String format = DateUtils.format(new Date(), "HH:00:00");
        modelWrapper.like("hour", format);
        List<SectionModelData> modelData = sectionModelDataService.list(modelWrapper);
        for (SectionInformation sectionInformation : list) {
            JSONObject json = new JSONObject();
            List<AttStBase> collect = data.stream().filter(d -> d.getStCode().equals(sectionInformation.getStcd())).collect(Collectors.toList());
            List<SectionModelData> collect1 = modelData.stream().filter(d -> d.getSectionid().equals(sectionInformation.getId())).collect(Collectors.toList());
            json.put("sectionName",sectionInformation.getName());
            if (CollectionUtils.isNotBlank(collect)){
                if (CollectionUtils.isNotBlank(collect1)){
                    json.put("waterLevel",collect1.get(0).getDepth());
                }else {
                    json.put("waterLevel","-");
                }
                json.put("warningLevel",collect.get(0).getWarningLevel());
                json.put("guaranteeLevel",collect.get(0).getGuaranteeLevel());
                json.put("updateTime",collect.get(0).getTime());
            }else {
                json.put("waterLevel","-");
                json.put("warningLevel","-");
                json.put("guaranteeLevel","-");
                json.put("updateTime","-");
            }
            result.add(json);
        }
        return CommonResult.success(result);
    }
}
