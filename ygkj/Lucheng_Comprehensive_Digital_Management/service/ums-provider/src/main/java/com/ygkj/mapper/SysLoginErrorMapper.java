package com.ygkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.entity.SysLoginError;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyf
 * @since 2021-03-23
 */
@Repository
public interface SysLoginErrorMapper extends BaseMapper<SysLoginError> {

    @Select("SELECT * FROM sys_login_error WHERE IP_ADDR = #{ipAddr} AND USER_ID = #{userId}")
    SysLoginError getLoginError(@Param("userId") String userId, @Param("ipAddr") String ipAddr);

    @Insert("INSERT INTO sys_login_error(ID, IP_ADDR, USER_ID, LAST_ERR_TIME, CONT_NUM, TOTAL_NUM) VALUES (#{id}, #{ipAddr}, #{userId}, #{date}, '1', '1')")
    void saveLogin(@Param("id") String id, @Param("userId") String userId, @Param("ipAddr") String ipAddr, @Param("date") String date);
}
