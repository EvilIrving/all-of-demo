package com.ygkj.river.service.impl;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttBeautLakesMapper;
import com.ygkj.river.service.AttBeautLakesService;
import com.ygkj.river.service.AttBeautVillageService;
import com.ygkj.river.service.AttRuralPondService;
import com.ygkj.river.service.AttSmallMediumRiverService;
import com.ygkj.river.vo.request.AttBeautLakesReqVo;
import com.ygkj.river.vo.request.AttBeautVillageReqVo;
import com.ygkj.river.vo.request.AttRuralPondReqVo;
import com.ygkj.river.vo.request.AttSmallMediumRiverReqVo;
import com.ygkj.river.vo.response.*;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hucong
 * @description 美丽河湖表
 * @date 2021-08-12
 */
@Service
public class AttBeautLakesServiceImpl implements AttBeautLakesService {

    @Resource
    private AttBeautLakesMapper attBeautLakesMapper;
    @Resource
    private AttBeautVillageService attBeautVillageService;
    @Resource
    private AttRuralPondService attRuralPondService;
    @Resource
    private AttSmallMediumRiverService attSmallMediumRiverService;

    @Override
    public PageVO<AttBeautLakesRespVo> loadPage(AttBeautLakesReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attBeautLakesMapper.loadPage(vo));
    }

    /**
     * 类型 1:工程总数 2:项目在建 3:已完工
     *
     * @param type
     * @return
     */
    @Override
    public BeautLakeRespVo projectStatic(String type) {
        BeautLakeRespVo beautLakeRespVo = new BeautLakeRespVo();
        Integer projectTotalNum = 0, underConstructionNum = 0, completeNum = 0, beautLakeNum = 0, ruralPondNum = 0, beautVillageNum = 0, smallMediumRiverNum = 0;

        //美丽河湖统计
        AttBeautLakesReqVo vo = new AttBeautLakesReqVo();
        vo.setContainWenzhou("1");
        List<AttBeautLakesRespVo> attBeautLakesRespVos = attBeautLakesMapper.loadPage(vo);
        projectTotalNum += attBeautLakesRespVos.size();

        underConstructionNum += attBeautLakesRespVos.stream().filter(e -> "项目在建".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        completeNum += attBeautLakesRespVos.stream().filter(e -> "已完工".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        List<AttBeautLakesRespVo> collect1 = attBeautLakesRespVos.stream().filter(e -> "温州市".equals(e.getCounty())).collect(Collectors.toList());
        if (!collect1.isEmpty()) {
            beautLakeRespVo.setBeautLakeProgress(collect1.get(0).getProgress());
        }

        //水美乡镇
        AttBeautVillageReqVo vo1 = new AttBeautVillageReqVo();
        vo.setContainWenzhou("1");
        List<AttBeautVillageRespVo> attBeautVillageRespVos = attBeautVillageService.loadPage(vo1).getList();
        projectTotalNum += attBeautVillageRespVos.size();

        underConstructionNum += attBeautVillageRespVos.stream().filter(e -> "项目在建".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        completeNum += attBeautVillageRespVos.stream().filter(e -> "已完工".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        List<AttBeautVillageRespVo> collect2 = attBeautVillageRespVos.stream().filter(e -> "温州市".equals(e.getCounty())).collect(Collectors.toList());
        if (!collect2.isEmpty()) {
            beautLakeRespVo.setBeautVillageNumProgress(collect2.get(0).getProgress());
        }

        //农村池塘
        AttRuralPondReqVo vo2 = new AttRuralPondReqVo();
        vo2.setContainWenzhou("1");
        List<AttRuralPondRespVo> attRuralPondRespVos = attRuralPondService.loadPage(vo2).getList();
        projectTotalNum += attRuralPondRespVos.size();

        underConstructionNum += attRuralPondRespVos.stream().filter(e -> "项目在建".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        completeNum += attRuralPondRespVos.stream().filter(e -> "已完工".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        List<AttRuralPondRespVo> collect3 = attRuralPondRespVos.stream().filter(e -> "温州市".equals(e.getCounty())).collect(Collectors.toList());
        if (!collect3.isEmpty()) {
            beautLakeRespVo.setRuralPondNumProgress(collect3.get(0).getProgress());
        }

        //中小河流
        AttSmallMediumRiverReqVo vo3 = new AttSmallMediumRiverReqVo();
        vo3.setContainWenzhou("1");
        List<AttSmallMediumRiverRespVo> attSmallMediumRiverRespVos = attSmallMediumRiverService.loadPage(vo3).getList();
        projectTotalNum += attSmallMediumRiverRespVos.size();

        underConstructionNum += attSmallMediumRiverRespVos.stream().filter(e -> "项目在建".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        completeNum += attSmallMediumRiverRespVos.stream().filter(e -> "已完工".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        List<AttSmallMediumRiverRespVo> collect4 = attSmallMediumRiverRespVos.stream().filter(e -> "温州市".equals(e.getCounty())).collect(Collectors.toList());
        if (!collect4.isEmpty()) {
            beautLakeRespVo.setSmallMediumRiverNumProgress(collect4.get(0).getProgress());
        }

        if ("1".equals(type)) {//类型 1:工程总数 2:项目在建 3:已完工
            beautLakeNum = attBeautLakesRespVos.size();
            beautVillageNum = attBeautVillageRespVos.size();
            ruralPondNum = attRuralPondRespVos.size();
            smallMediumRiverNum = attSmallMediumRiverRespVos.size();
        } else if ("2".equals(type)) {
            beautLakeNum = attBeautLakesRespVos.stream().filter(e -> "项目在建".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
            beautVillageNum = attBeautVillageRespVos.stream().filter(e -> "项目在建".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
            ruralPondNum = attRuralPondRespVos.stream().filter(e -> "项目在建".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
            smallMediumRiverNum = attSmallMediumRiverRespVos.stream().filter(e -> "项目在建".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        } else if ("3".equals(type)) {
            beautLakeNum = attBeautLakesRespVos.stream().filter(e -> "已完工".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
            beautVillageNum = attBeautVillageRespVos.stream().filter(e -> "已完工".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
            ruralPondNum = attRuralPondRespVos.stream().filter(e -> "已完工".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
            smallMediumRiverNum = attSmallMediumRiverRespVos.stream().filter(e -> "已完工".equals(e.getProjectStatus())).collect(Collectors.toList()).size();
        }

        beautLakeRespVo.setProjectTotalNum(projectTotalNum);
        beautLakeRespVo.setUnderConstructionNum(underConstructionNum);
        beautLakeRespVo.setCompleteNum(completeNum);
        beautLakeRespVo.setBeautLakeNum(beautLakeNum);
        beautLakeRespVo.setRuralPondNum(ruralPondNum);
        beautLakeRespVo.setBeautVillageNum(beautVillageNum);
        beautLakeRespVo.setSmallMediumRiverNum(smallMediumRiverNum);
        return beautLakeRespVo;
    }

    /**
     * 类型 美丽河湖、中小河流、水美乡镇、农村池塘
     *
     * @param type
     * @return
     */
    @Override
    public EachProjectStatisticsRespVO eachProjectStatistics(String type) {
        EachProjectStatisticsRespVO entity = new EachProjectStatisticsRespVO();
        Integer total = 0;
        switch (type) {
            case "美丽河湖":
                List<JSONObject> list = attBeautLakesMapper.eachProjectStatistics();
                for (JSONObject jsonObject : list) {
                    String projectStatus = jsonObject.getStr("project_status", "");
                    Integer num = jsonObject.getInt("num", 0);
                    total += num;
                    if ("已完工".equals(projectStatus)) {
                        entity.setCompleteNum(num);
                    } else if ("项目在建".equals(projectStatus)) {
                        entity.setUnderConstructionNum(num);
                    }
                }
                break;
            case "中小河流":
                List<JSONObject> list2 = attBeautLakesMapper.attSmallMediumRiverService();
                for (JSONObject jsonObject : list2) {
                    String projectStatus = jsonObject.getStr("project_status", "");
                    Integer num = jsonObject.getInt("num", 0);
                    total += num;
                    if ("已完工".equals(projectStatus)) {
                        entity.setCompleteNum(num);
                    } else if ("项目在建".equals(projectStatus)) {
                        entity.setUnderConstructionNum(num);
                    }
                }
                break;
            case "水美乡镇":
                List<JSONObject> list3 = attBeautVillageService.attSmallMediumRiverService();
                for (JSONObject jsonObject : list3) {
                    String projectStatus = jsonObject.getStr("project_status", "");
                    Integer num = jsonObject.getInt("num", 0);
                    total += num;
                    if ("已完工".equals(projectStatus)) {
                        entity.setCompleteNum(num);
                    } else if ("项目在建".equals(projectStatus)) {
                        entity.setUnderConstructionNum(num);
                    }
                }
                break;
            case "农村池塘":
                List<JSONObject> list4 = attRuralPondService.attSmallMediumRiverService();
                for (JSONObject jsonObject : list4) {
                    String projectStatus = jsonObject.getStr("project_status", "");
                    Integer num = jsonObject.getInt("num", 0);
                    total += num;
                    if ("已完工".equals(projectStatus)) {
                        entity.setCompleteNum(num);
                    } else if ("项目在建".equals(projectStatus)) {
                        entity.setUnderConstructionNum(num);
                    }
                }
                break;
        }
        entity.setProjectTotalNum(total);
        return entity;
    }
}
