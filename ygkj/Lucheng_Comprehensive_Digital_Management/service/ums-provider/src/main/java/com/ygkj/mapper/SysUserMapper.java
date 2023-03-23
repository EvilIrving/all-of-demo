package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SysUser;
import com.ygkj.platform.management.model.SysLoginLog;
import com.ygkj.vo.req.LoginReqVO;
import com.ygkj.vo.req.UserPageReqVO;
import com.ygkj.vo.req.WyhUserVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    List<SysUser> selectUserInfoByDeptIds(List<String> deptIds);

    List<SysUser> selectWaterManageUserListByDeptIds(@Param("deptIds") List<String> deptIds,@Param("key") String key ,@Param("flag") Boolean flag);

    List<SysUser> getUserListByDeptId(@Param("deptId") String deptId);

    List<SysUser> getUserListByDeptIdAndName(@Param("deptId") String deptId, @Param("username") String username);

    String selectUserNameById(@Param("id") String id);

    int deletedUsers(@Param("sysUser") SysUser sysUser, @Param("list") List<String> list);

    int updateByDeptId(@Param("status") Integer status, @Param("deptId") String deptId);

    void batchInsert(List<SysUser> list);

    void batchUpdate(List<SysUser> updateList);

    List<SysUser> selectByIds(List<String> list);

    LoginReqVO selectByPhone(String phone);

    List<com.ygkj.auth.api.user.model.SysUser> findByIds(String userIds);

    /**
     * 查找当前请求的用户在授权的部门，大于0，则是；反之不是
     *
     * @param deportIds 部门ids
     * @param userId    请求数据用户id
     * @return
     */
    Integer findDeportUser(String deportIds, String userId);

    @Insert("INSERT INTO sys_login_log (user_id,user_name,dept_id,department,login_ip,login_time) VALUES (#{userId},#{userName},#{deptId},#{department},#{loginIp},#{loginTime})")
    int insertLoginLog(SysLoginLog sysLoginLog);

    @Select("SELECT user_name,phone FROM lc_basic_db.att_block_authoriza WHERE del_flag = 0 AND type = '2'")
    List<SysUser> selectBlockUser(String phone);

    @Select("  SELECT phone AS username,password FROM sys_user WHERE id = #{id} AND deleted = 1")
    LoginReqVO selectByUserCode(String id);

    @Select("delete from sys_user where dept_id = #{wyhId}")
    void delWyhPerson(@Param("wyhId") String wyhId);

    List<WyhUserVo> getWyhUserList(@Param("wyhUserVo") WyhUserVo wyhUserVo);

    @Select("delete from sys_user where id = #{id}")
    void delWyhUser(@Param("id") String id);

    @Select("select * from sys_user user left join professional_company company on user.dept_id = company.id where user.phone = #{phone}")
    JSONObject getCompanybyPhone(String phone);

    List<com.ygkj.auth.api.user.model.SysUser> selectUserByIdList(List<String> idList);

    @Select("select id from sys_dept_water_manage")
    List<String> selectWaterManageDeptIds();

    @Update("update sys_user set job_id = #{jobIds} where ID= #{userId}")
    void bindUserJob(@Param("userId") String userId, @Param("jobIds") String jobIds);

    @Select("select * from sys_user where job_id like concat('%',#{jobId},'%')")
    List<SysUser> loadUserByJob(String jobId);

    @Update("update sys_user set status = #{status} where id = #{id}")
    int enableUser(@Param("id")String id, @Param("status") String status);
}