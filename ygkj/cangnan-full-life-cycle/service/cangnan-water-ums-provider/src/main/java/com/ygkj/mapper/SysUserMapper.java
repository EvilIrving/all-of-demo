package com.ygkj.mapper;

import com.ygkj.entity.SysUser;
import com.ygkj.vo.req.LoginReqVO;
import com.ygkj.vo.req.UserPageReqVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser getUserInfoByName(String username);

    List<SysUser> selectAll(UserPageReqVO vo);

    List<SysUser> selectUserInfoByDeptIds (List<String> deptIds);

    List<SysUser> getUserListByDeptId(@Param("deptId") String deptId);

    List<SysUser> getUserListByDeptIdAndName(@Param("deptId") String deptId, @Param("username") String username);

    String selectUserNameById(@Param("id")String id);

    int deletedUsers(@Param("sysUser") SysUser sysUser,@Param("list") List<String> list);

    int updateByDeptId(@Param("status") Integer status, @Param("deptId") String deptId);

    void batchInsert(List<SysUser> list);

    void batchUpdate(List<SysUser> updateList);

    List<SysUser> selectByIds(List<String> list);

    LoginReqVO selectByPhone(String phone);
}