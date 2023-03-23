package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.DataMaintenance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author wgf
 * @date 2022/4/24 10:42
 * @Description:
 */
@Repository
public interface DataMaintenanceMapper extends BaseMapper<DataMaintenance> {

    @Select("select name from bm_project where PRCD = #{prcd}")
    String getByPrcdName(@Param("prcd") String prcd);
}