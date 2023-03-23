package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.OverseeEvaluation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


/**
 * @author wgf
 * @date 2022/6/10 11:25
 * @Description:
 */
@Repository
public interface OverseeEvaluationMapper extends BaseMapper<OverseeEvaluation> {

    @Select("SELECT username FROM sys_user WHERE id = #{userId}")
    String getUserName(@Param("userId") String userId);

    @Select("select * from sys_dept_water_manage where id = #{deptId}")
    String getWaterDept(@Param("deptId") String deptId);
}
