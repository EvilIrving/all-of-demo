package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.request.ProjectQueryVo;
import com.ygkj.gragh.vo.request.RiverProjectQueryVo;
import com.ygkj.gragh.vo.request.RiverQueryVo;
import org.apache.ibatis.annotations.ResultType;

import java.util.List;

public interface ProjectMapper {

    List<AttResBase> selectReservoirByCondition(ProjectQueryVo queryVo);

    List<AttPustBase> selectPumpByCondition(ProjectQueryVo queryVo);

    List<AttWagaBase> selectWagaByCondition(ProjectQueryVo queryVo);

    List<AttSeawallBase> selectSeawallByCondition(ProjectQueryVo queryVo);

    List<AttDikeBase> selectDikeByCondition(ProjectQueryVo queryVo);

    List<AttMnpdBase> selectMountainPoolByCondition(ProjectQueryVo queryVo);

    List<AttHystBase> selectHydristByCondition(ProjectQueryVo queryVo);

    List<WaterSupplyProject> selectWaterSupplyProjectByCondition(ProjectQueryVo queryVo);

    List<BuildingProject> selectBuildingProjectByCondition(ProjectQueryVo queryVo);

    List<AttProjBase> selectRiverProject(RiverProjectQueryVo riverProjectQueryVo);

    /**
     * 更新海塘
     *
     * @author xq
     * @date 2021/05/18
     **/
    int updateSeawall(AttSeawallBase attSeawallBase);

    /**
     * 更新水库
     *
     * @param attResBase
     * @return
     */
    int updateRsvr(AttResBase attResBase);
}
