package com.ygkj.aspects.mapper;

import com.ygkj.platform.management.model.SysApiLog;
import com.ygkj.platform.management.model.SysApiParamLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface ApiLogMapper {

    @Insert("INSERT INTO lc_basic_db.sys_api_log " +
            "(sys_code,sys_name,user_id,user_name,dept_id,department,api_name,api_code,response_time,request_time,source_ip,parameters,code,response_status) " +
            "VALUES (#{sysCode},#{sysName},#{userId},#{userName},#{deptId},#{department},#{apiName},#{apiCode},#{responseTime}" +
            ",#{requestTime},#{sourceIp},#{parameters},#{code},#{responseStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    long insertApiLog(SysApiLog sysApiLog);

    @Insert("INSERT INTO lc_basic_db.sys_api_param_log " +
            "(api_log_id,parameter_name,parameter_value,user_id) VALUES(#{apiLogId},#{parameterName},#{parameterValue},#{userId})")
    void insertApiParameterLog(SysApiParamLog sysApiParamLog);
}
