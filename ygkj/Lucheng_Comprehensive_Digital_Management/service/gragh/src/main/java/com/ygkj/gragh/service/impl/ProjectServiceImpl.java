package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.ProjectMapper;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.service.ProjectService;
import com.ygkj.gragh.utils.DistanceUtil;
import com.ygkj.gragh.vo.request.ProjectQueryVo;
import com.ygkj.gragh.vo.request.ProjectRangeQueryVo;
import com.ygkj.gragh.vo.request.RiverProjectQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public CommonResult projectList(ProjectQueryVo queryVo) {
        switch (queryVo.getType()) {
            case "大中型水库":
            case "小型水库":
                return CommonResult.success(projectMapper.selectReservoirByCondition(queryVo));
            case "泵站":
                return CommonResult.success(projectMapper.selectPumpByCondition(queryVo));
            case "水闸":
                return CommonResult.success(projectMapper.selectWagaByCondition(queryVo));
            case "海塘":
                return CommonResult.success(projectMapper.selectSeawallByCondition(queryVo));
            case "堤防":
                return CommonResult.success(projectMapper.selectDikeByCondition(queryVo));
            case "山塘":
                return CommonResult.success(projectMapper.selectMountainPoolByCondition(queryVo));
            case "水电站":
                return CommonResult.success(projectMapper.selectHydristByCondition(queryVo));
            case "农村供水工程":
                return CommonResult.success(projectMapper.selectWaterSupplyProjectByCondition(queryVo));
            case "在建工程":
                return CommonResult.success(projectMapper.selectBuildingProjectByCondition(queryVo));
            default:
                return CommonResult.success(null);
        }
    }

    @Override
    public CommonResult projectsInRange(ProjectRangeQueryVo projectRangeQueryVo) {
        ProjectQueryVo projectQueryVo = new ProjectQueryVo();
        projectQueryVo.setType(projectRangeQueryVo.getType());
        List<Object> projects = (List<Object>) (projectList(projectQueryVo).getData());
        List<Object> result = new ArrayList<>();
        double latitude = 0d, longitude = 0d;
        for (Object project : projects) {
            if (project instanceof AttResBase) {
                latitude = ((AttResBase) project).getLowLeftLat().doubleValue();
                longitude = ((AttResBase) project).getLowLeftLong().doubleValue();
            } else if (project instanceof AttDikeBase) {
                latitude = ((AttDikeBase) project).getStartLat().doubleValue();
                longitude = ((AttDikeBase) project).getStartLong().doubleValue();
            } else if (project instanceof AttHystBase) {
                latitude = ((AttHystBase) project).getHystLat().doubleValue();
                longitude = ((AttHystBase) project).getHystLong().doubleValue();
            } else if (project instanceof AttMnpdBase) {
                latitude = ((AttMnpdBase) project).getDtMidLat().doubleValue();
                longitude = ((AttMnpdBase) project).getDtMidLong().doubleValue();
            } else if (project instanceof AttWagaBase) {
                latitude = ((AttWagaBase) project).getStartLat().doubleValue();
                longitude = ((AttWagaBase) project).getStartLong().doubleValue();
            } else if (project instanceof AttSeawallBase) {
                latitude = Double.parseDouble(((AttSeawallBase) project).getLatitude());
                longitude = Double.parseDouble(((AttSeawallBase) project).getLongitude());
            } else if (project instanceof AttPustBase) {
                latitude = ((AttPustBase) project).getPustLat().doubleValue();
                longitude = ((AttPustBase) project).getPustLong().doubleValue();
            } else if (project instanceof WaterSupplyProject) {
                latitude = Double.parseDouble(((WaterSupplyProject) project).getLatitude());
                longitude = Double.parseDouble(((WaterSupplyProject) project).getLongitude());
            } else if (project instanceof BuildingProject) {
                latitude = Double.parseDouble(((BuildingProject) project).getLatitude());
                longitude = Double.parseDouble(((BuildingProject) project).getLongitude());
            }
            BigDecimal distance = DistanceUtil.getDistance(latitude, longitude, projectRangeQueryVo.getLatitude(), projectRangeQueryVo.getLongitude());
            double index = Math.abs(distance.doubleValue());
            if (index <= projectRangeQueryVo.getDistance()) {
                result.add(project);
            }
        }
        return CommonResult.success(result);
    }

    @Override
    public List<AttProjBase> riverProjects(RiverProjectQueryVo riverProjectQueryVo) {
        return projectMapper.selectRiverProject(riverProjectQueryVo);
    }
}
