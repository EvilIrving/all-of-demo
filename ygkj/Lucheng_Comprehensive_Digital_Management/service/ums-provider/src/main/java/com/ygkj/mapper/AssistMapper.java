package com.ygkj.mapper;

import com.ygkj.platform.management.model.SysLoginLog;
import com.ygkj.warning.model.SysSendMessageLog;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 辅助mapper
 */
public interface AssistMapper {

    @Select("select a.* FROM " +
            "(SELECT * FROM lc_business_db.sys_send_message_log WHERE from_user_id = #{userId} " +
            "UNION ALL " +
            "SELECT * FROM lc_business_db.sys_send_message_log WHERE destination_user_id = #{userId}) a")
    List<SysSendMessageLog> mineReceiveAndSend(String userId);

    @Select("SELECT COUNT(1) FROM lc_business_db.sys_user_collection WHERE user_id = #{userId}")
    int userCollectionNumbers(String userId);

    @Select("SELECT count( 1 ) AS frequentlyVisitedNumber ,sys_name as frequentlyVisitedSystem FROM sys_api_log WHERE " +
            "user_id = #{userId} " +
            "GROUP BY sys_name ORDER BY count( 1 ) DESC LIMIT 1")
    Map<String, Object> frequentlyVisited(String userId);

    @Select("SELECT * FROM sys_login_log WHERE user_id = #{userId} ORDER BY login_time DESC LIMIT 1")
    SysLoginLog lastLoginLog(String userId);
}
