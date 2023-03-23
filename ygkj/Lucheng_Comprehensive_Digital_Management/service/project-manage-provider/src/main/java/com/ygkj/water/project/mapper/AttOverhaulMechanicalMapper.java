package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.AttOverhaulMechanical;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2022-04-18 14:24
 * @description 机械操作
 */
public interface AttOverhaulMechanicalMapper extends BaseMapper<AttOverhaulMechanical> {

    @Update("update att_overhaul_mechanical set del_flag = 1 where id = #{id}")
    int deleteByMechanicalId(@Param("id") String id);
}
