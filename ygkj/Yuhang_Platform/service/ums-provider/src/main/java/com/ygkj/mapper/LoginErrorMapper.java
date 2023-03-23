package com.ygkj.mapper;

import com.ygkj.entity.LoginError;
import org.apache.ibatis.annotations.Param;

public interface LoginErrorMapper {

    LoginError getLoginError(@Param("phone") String phone,@Param("ipAddr") String ipAddr);

    void updateById(LoginError loginError);

    void insert(LoginError loginError);

}
