package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.MtDmPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2022-04-18 17:03
 * @description
 */
public interface MtDmPlanMapper extends BaseMapper<MtDmPlan> {

    @Update("update mt_dm_plan set del_flag = 1 where id = #{id}")
    int deleteByMtDmId(@Param("id") String id);
}
