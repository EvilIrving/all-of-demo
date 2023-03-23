package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.MtDailyTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2022-04-19 9:18
 * @description
 */
public interface MtDailyTaskMapper extends BaseMapper<MtDailyTask> {

    @Update("update mt_daily_task set del_flag = 1 where id = #{id}")
    int deleteByTaskId(@Param("id") String id);

    @Update("update mt_daily_task set hidden_danger_status = #{code} where id = #{id}")
    int updateStatus(@Param("id") String taskId, @Param("code") Integer code);

    @Select("select id from sys_user where phone = #{phone}")
    String findUserIdByPhone(@Param("phone") String phone);
}
