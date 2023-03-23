package com.ygkj.river.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.river.model.AttBlueProject;
import com.ygkj.river.vo.request.AttBlueProjectReqVo;
import com.ygkj.river.vo.response.AttBlueProjectResVo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author lxl
 * @create 2021-08-06 9:34
 * @description
 */
public interface AttBlueProjectMapper extends BaseMapper<AttBlueProject> {

    /**
     * 条件查询对应的 列表
     *
     * @param reqVo
     * @return
     */
    List<AttBlueProjectResVo> selectByCondition(AttBlueProjectReqVo reqVo);

    /**
     * 批后监管按流域分布统计
     *
     * @return
     */
    List<JSONObject> projectStatisByBasin();

    /**
     * 批后监管按河道分布统计
     *
     * @return
     */
    List<JSONObject> projectStatisByRiverLevel();

    /**
     * 批后监管按行政区划统计
     *
     * @return
     */
    List<JSONObject> projectStatisByAdcd();

    /**
     * 批后监管按项目阶段统计
     *
     * @return
     */
    List<JSONObject> projectStatisByProjectStage();

    @Select("select count(1) from att_blue_project where del_flag = 0")
    Integer countProjectNum();

    /**
     * 根据项目id 更新项目监管状态
     *
     * @param regulatory
     * @param projectId
     * @return
     */
    @Update("update att_blue_project set regulatory_status = #{regulatory} where id = #{projectId}")
    Integer updateRegulatoryStatusById(@Param("regulatory") Integer regulatory, @Param("projectId") String projectId);

    AttBlueProject findById(@Param("id") String id);

    List<String> selectRiverIdOfWaterAreaOccupyFillNotBalance(String riverId);

    @Update("update att_blue_project set project_stage = #{stage} where id = #{projectId}")
    Integer updateProjectStatus(@Param("stage") Integer stage, @Param("projectId") String projectId);

}
