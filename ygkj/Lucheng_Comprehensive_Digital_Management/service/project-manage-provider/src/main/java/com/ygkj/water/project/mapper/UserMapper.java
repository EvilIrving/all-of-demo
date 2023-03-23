package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.project.model.SysUser;
import com.ygkj.project.vo.response.DeptUserInfo;
import com.ygkj.project.vo.response.SysUserSimpleResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@DS("main_db")
public interface UserMapper {

    @DS("main_db")
    List<SysUser> load(String ids);

    @DS("main_db")
    List<DeptUserInfo> getDeptInfos();

    @DS("main_db")
    List<SysUser> getUsersByDeptId(String deptId);

    @DS("main_db")
    List<SysUser> getUserByIds(List<String> userIds);

    @DS("main_db")
    List<DeptUserInfo> getDepts();

    @DS("main_db")
    List<SysUserSimpleResVo> getUsers();

    @DS("main_db")
    Integer constructionManagementPerson(@Param("userID") String userID);

    @Select("select 1 from sys_user where deleted=1 and phone=#{phone}")
    Integer checkPhoneExist(String phone);
}
