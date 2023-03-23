package com.ygkj.water.project.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("exchange")
public interface BhMapper {

    /**
     * 工程简介
     *
     * @param seawallCode
     * @return
     */
    String introduction(String seawallCode);

    /**
     * 组织架构
     *
     * @param seawallCode
     * @return
     */
    List<JobSettingResVo> organization(String seawallCode);

    /**
     * 组织架构详细信息
     *
     * @param reqVo
     * @return
     */
    List<JobSettingDetailResVo> organizationDetail(JobSettingReqVo reqVo);

    /**
     * 人员信息
     *
     * @param reqVo
     * @return
     */
    List<JobPersonResVo> listPeople(JobPersonReqVo reqVo);

    /**
     * 年度培训
     *
     * @param reqVo
     * @return
     */
    List<PersonTrainResVo> personTrain(PersonTrainReqVo reqVo);

    /**
     * 管护经费
     *
     * @param reqVo
     * @return
     */
    List<ManageFundResVo> managefund(ManageFundReqVo reqVo);

    /**
     * 管理制度附件
     *
     * @param seawallCode
     * @return
     */
    List<ManageinstitutionResVo> manageinstitution(String seawallCode);

    /**
     * 项目维护
     *
     * @param reqVo
     * @return
     */
    List<MfpmResVo> mfpm(MfpmReqVo reqVo);

    /**
     * 资金申报
     *
     * @param reqVo
     * @return
     */
    List<MfdResVo> mfd(MfdReqVo reqVo);

    /**
     * 资金使用
     *
     * @param reqVo
     * @return
     */
    List<MfuResVo> mfu(MfuReqVo reqVo);

    /**
     * 根据id 查询姓名
     * @param uid
     * @return
     */
    @Select("select real_name from bh_user where find_in_set(u_id,#{uid})")
    List<String> selectUserList(@Param("uid") String uid);

    /**
     * 查询标化用户列表
     * @param reqVo
     * @return
     */
    List<BhUserResVo> selectBhUserListByReqVo(@Param("reqVo") BhDeptUserReqVo reqVo);

    List<JSONObject> selectBhDeptListByReqVo(@Param("reqVo") BhDeptUserReqVo reqVo);

    /**
     * 根据id 查询天地图数据
     * @param id
     * @return
     */
    @Select("select `position` from bh_sl_patrol_gis_info where id = #{id}")
    String selectPositionGisInfo(@Param("id") String id);


    @Select("select group_concat(u_id) from bh_user where find_in_set( real_name,#{names}) ")
    String findIdsByName(String names);
}
