package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.lcsyn.model.SynZzdUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2022-05-26 16:21
 * @description
 */
@DS("basic")
public interface SynZzdUserMapper extends BaseMapper<SynZzdUser> {


    @Select("select id from sys_user where id = #{userCode}")
    String findByUserCode(String userCode);

    @Insert("insert into sys_user (`id`,`username`,`salt`,`password`,`phone`,`dept_id`,`email`," +
            "`user_type`,`job`,`sort`) values " +
            "( #{userCode},#{trueName},#{salt},#{password},#{mobile},#{dept},#{mail},#{userType},#{job},#{orderId} )")
    int insertSysUser(SynZzdUser synZzdUser);

    @Update("update sys_user set `username`=#{trueName}, `phone`= #{mobile} , `dept_id`= #{dept}, `email` = #{mail}," +
            " `job` = #{job} ,`sort` = #{orderId} , deleted = 1 where id = #{userCode} ")
    int updateSysUser(SynZzdUser synZzdUser);

    @Update("update sys_user set deleted = 0 where id = #{userCode}")
    Integer deleteByUserCode(@Param("userCode") String userCode);

    @Update("update sys_user set deleted = 0 where dept_id = #{dept} and id not in (select * from (select id from sys_user where find_in_set(id,#{ids}) ) a )  ")
    Integer deletedUserByIds(String dept, String ids);
}
