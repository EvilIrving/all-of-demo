package com.ygkj.gragh.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.gragh.model.FloodControlDispatchingPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface FloodControlDispatchingPlanMapper extends BaseMapper<FloodControlDispatchingPlan> {
    @Select("SELECT * FROM water_level_control WHERE start_time <= DATE_FORMAT(NOW(), '%m-%d') AND end_Time >= DATE_FORMAT(NOW(), '%m-%d') and prcd = #{prcd}")
    JSONObject kyWaterLevel(@Param("prcd") String prcd);
}
