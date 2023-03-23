package com.ygkj.water.project.mapper;

import com.ygkj.project.vo.request.AllProjectReqVo;
import com.ygkj.project.vo.request.DelProjectReqVo;
import com.ygkj.project.vo.request.ProjectEditReqVo;
import com.ygkj.project.vo.response.AllProjectResVo;
import com.ygkj.project.vo.response.DictResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface EngineeringSupervisionMapper {

    /**
     * 工程类型统计
     *
     * @param colName
     * @return
     */
    List<DictResVo> countProject(String colName);

    /**
     * 工程列表
     *
     * @param allProjectReqVo
     * @return
     */
    List<AllProjectResVo> listProject(AllProjectReqVo allProjectReqVo);

    /**
     * 工程详细信息
     *
     * @param projectCode
     * @return
     */
    AllProjectResVo projectDetail(String projectCode);

    /**
     * 更新线图层点位
     *
     * @param projectEditReqVo
     */
    @Update("UPDATE att_${projectType}_base SET points = #{points} WHERE ${projectType}_code = #{projectCode}")
    void updateLine(ProjectEditReqVo projectEditReqVo);

    /**
     * 更新水库经纬度
     *
     * @param projectEditReqVo
     */
    @Update("UPDATE att_res_base SET low_left_long = #{longitude},low_left_lat=#{latitude} WHERE res_code = #{projectCode}")
    void updateResPoint(ProjectEditReqVo projectEditReqVo);

    /**
     * 更新水电站经纬度
     *
     * @param projectEditReqVo
     */
    @Update("UPDATE att_hyst_base SET hyst_long = #{longitude},hyst_lat=#{latitude} WHERE hyst_code = #{projectCode}")
    void updateHyPoint(ProjectEditReqVo projectEditReqVo);

    /**
     * 更新泵站经纬度
     *
     * @param projectEditReqVo
     */
    @Update("UPDATE att_pust_base SET pust_long = #{longitude},pust_lat=#{latitude} WHERE pust_code = #{projectCode}")
    void updatePuPoint(ProjectEditReqVo projectEditReqVo);

    /**
     * 更新水闸经纬度
     *
     * @param projectEditReqVo
     */
    @Update("UPDATE att_waga_base SET start_long = #{longitude},start_lat=#{latitude} WHERE waga_code = #{projectCode}")
    void updateWaPoint(ProjectEditReqVo projectEditReqVo);

    /**
     * 更新山塘经纬度
     *
     * @param projectEditReqVo
     */
    @Update("UPDATE att_mnpd_base SET dt_mid_long = #{longitude},dt_mid_lat=#{latitude} WHERE mnpd_code = #{projectCode}")
    void updateMnPoint(ProjectEditReqVo projectEditReqVo);

    /**
     * 查询线图层点位数组
     *
     * @param projectType
     * @param projectCode
     * @return
     */
    @Select("SELECT points FROM att_${projectType}_base WHERE ${projectType}_code = #{projectCode}")
    String queryPoints(@Param("projectType") String projectType, @Param("projectCode") String projectCode);

    /**
     * 批量删除工程
     *
     * @param reqVo
     */
    void delProject(DelProjectReqVo reqVo);

    /**
     * 批量删除其他工程
     *
     * @param reqVo
     */
    void delOtherProject(DelProjectReqVo reqVo);

    List<DictResVo> countProjectNew(@Param("colName") String colName, @Param("areaCode") String areaCode);
}
