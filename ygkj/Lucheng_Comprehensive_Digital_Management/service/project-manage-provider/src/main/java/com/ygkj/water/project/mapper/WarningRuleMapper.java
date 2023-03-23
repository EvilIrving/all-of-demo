package com.ygkj.water.project.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.project.model.ReminderTimeRecord;
import com.ygkj.project.model.WarningRule;
import com.ygkj.project.vo.request.RuleStReqVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author wgf
 * @date 2021/10/22 15:11
 * @Description:
 */
@Repository
public interface WarningRuleMapper  extends BaseMapper<WarningRule> {

    List<JSONObject> getSeawallByIds(@Param("asList") List<String> asList);

    List<JSONObject> getStationByIds(@Param("asList") List<String> asList);

    List<AttStBase> getStList(@Param("attStBase") AttStBase attStBase);

    List<AttStBase> listRuleSt(RuleStReqVo reqVo);

    @Select("select * from reminder_time_record where id = #{id}")
    ReminderTimeRecord getById(@Param("id") String id);

    @Insert("INSERT INTO reminder_time_record (`id`, `tm`) VALUES (#{id}, #{date})")
    void saveReminderTimeRecord(@Param("id")String id, @Param("date")Date date);

    @Update("UPDATE reminder_time_record SET `tm` = #{date} WHERE `id` = #{id}")
    void updateReminderTimeRecord(@Param("id")String id, @Param("date")Date date);

    List<JSONObject> projectList(String projectName);

    List<JSONObject> getProjectByIds(@Param("asList") List<String> asList);
}
