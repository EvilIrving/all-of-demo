package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.AttOverhaulElectrical;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2022-04-18 14:25
 * @description 电力操作
 */
public interface AttOverhaulElectricalMapper extends BaseMapper<AttOverhaulElectrical> {

    @Update("update att_overhaul_electrical set del_flag = 1 where id = #{id}")
    int deleteByElectricalId(@Param("id") String id);
}
