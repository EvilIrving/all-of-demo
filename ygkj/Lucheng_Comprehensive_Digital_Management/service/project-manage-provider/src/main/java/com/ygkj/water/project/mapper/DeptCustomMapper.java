package com.ygkj.water.project.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.DeptCustom;
import com.ygkj.project.model.SysDept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author wgf
 * @date 2021/10/21 17:02
 * @Description:
 */
@Repository
public interface DeptCustomMapper extends BaseMapper<DeptCustom> {

    @Select("SELECT getChildDeptId(#{id})")
    String getDeptIdsString(@Param("id") String id);

    @Select("select * from sys_dept where id = #{id}")
    SysDept getZzdDeptById(@Param("id") String id);

    @Select("select * from sys_dept where find_in_set(pid ,#{id})")
    List<SysDept> getDeptList(@Param("id") String id);

    @Select("SELECT getChildZzdDeptId(#{deptid})")
    String deptZzdIds(@Param("deptid")String deptid);
}
