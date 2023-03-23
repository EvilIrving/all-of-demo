package com.ygkj.river.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.supervision.model.SysUser;
import com.ygkj.supervision.vo.response.DeptRespNodeVO;
import com.ygkj.supervision.vo.response.SysUserResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {


    @Select("select id,dept_no,name as title,pid,status,relation_code,IFNULL(adcd,9999) adcd,order_id from sys_dept ")
    List<DeptRespNodeVO> selectDeptUserByRelations();

    @Select("select id,username as userName,dept_id,phone,real_name,nick_name from sys_user " +
            "where dept_id = #{deptId} and deleted = 1 and username like concat('%',#{key},'%')")
    List<SysUserResVo> selectListByDeptVo(@Param("deptId") String deptId, @Param("key") String key);

    @Select("select id,username as userName,dept_id,phone,real_name,nick_name from sys_user " +
            "where find_in_set(id,#{ids})")
    List<SysUserResVo> selectListByIds(@Param("ids")String ids);
}
