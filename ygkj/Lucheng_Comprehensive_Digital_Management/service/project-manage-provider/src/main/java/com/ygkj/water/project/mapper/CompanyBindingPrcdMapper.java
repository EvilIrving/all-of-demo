package com.ygkj.water.project.mapper;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.CompanyBindingPrcd;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 物业化公司绑定工程表 Mapper 接口
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
@Repository
public interface CompanyBindingPrcdMapper extends BaseMapper<CompanyBindingPrcd> {

    @Select("<script> SELECT project_code, project_name, longitude, latitude, project_type,adcd FROM att_real_all_project_base WHERE project_type != '河道' " +
            "<if test='null != name and name != \"\"'> AND project_name like concat('%',#{name},'%')</if>"+
            "<if test = 'type != null and type != \"\"'> AND project_type = #{type}</if>" +
            "</script>")
    List<JSONObject> getAllProject(@Param("name") String name, @Param("type") String type);
}
