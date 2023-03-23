package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.lcsyn.model.SynZzdDept;
import org.apache.ibatis.annotations.*;

/**
 * @author lxl
 * @create 2022-05-26 11:21
 * @description
 */
@DS("basic")
public interface SynZzdDeptMapper extends BaseMapper<SynZzdDept> {

    @Select("select id from sys_dept where id = #{deptCode}")
    String selectSysDept(String deptCode);

    @Update("update sys_dept set `name` = #{syn.name},`pid` = #{syn.parentId} ,`adcd` = #{syn.adcd} where id = #{syn.organizationId}")
    int updateSysDept(@Param("syn") SynZzdDept synZzdDept);

    @Insert("insert into sys_dept (`id`,`name`,`pid`,`status`,`adcd`,`order_id`) values" +
            " (#{syn.organizationId},#{syn.name},#{syn.parentId},1,#{syn.adcd},#{syn.orderId})")
    int insertSysDept(@Param("syn") SynZzdDept synZzdDept);

    @Delete("update sys_dept set deleted = 0 where id = #{organizationId}")
    Integer deleteDeptWaterByCode(@Param("organizationId") String organizationId);

}
