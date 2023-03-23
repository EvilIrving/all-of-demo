package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.MessageReminder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReminderMapper extends BaseMapper<MessageReminder> {

    @Select("select count(1) from message_reminder where reminder_user = #{userId} and `type` = #{type} and `status` = #{status}")
    Integer findMsgTotal(@Param("userId") String userId, @Param("type") String value,@Param("status") String value1);
}
