package com.ygkj.mapper;

import com.ygkj.entity.DingDingUser;
import com.ygkj.entity.InvokeLog;
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

    /**
     * 查找当前请求的用户在授权的部门，大于0，则是；反之不是
     * @param deportIds 部门ids
     * @param userId 请求数据用户id
     * @return
     */
    Integer findDeportUser(String deportIds, String userId);

    /**
     * 查找不在ids范围内的用户list
     * @param list
     * @return
     */
    List<SysUser> findUserNotInIds(List<String> list);

    List<com.ygkj.auth.api.user.model.SysUser> findByIds(String userIds);

    LoginReqVO selectByDingUserId(String accountId);

    LoginReqVO selectOne();

    List<SysUser> listAllUser();

    List<DingDingUser> listAllDingUser();


    void insertInvokeLog(InvokeLog invokeLog);
}