package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.project.model.RelWarnTemplateObj;
import com.ygkj.project.model.SysWarnGroup;
import com.ygkj.project.model.SysWarnLog;
import com.ygkj.project.model.SysWarnTemplate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/14
 */
@DS("main")
public interface WarningMapper {

    @Select("select id,name,task_class from sys_warn_group where task_class=#{taskClass} and del_flag=0 limit 1")
    @ResultType(SysWarnGroup.class)
    SysWarnGroup selectGroupByClassName(@Param("taskClass") String taskClass);

    @Select("select id,obj_id,group_id,template_id from rel_warn_template_obj where obj_id=#{objId} and group_id=#{groupId} limit 1")
    @ResultType(RelWarnTemplateObj.class)
    RelWarnTemplateObj selectRelWarnTemplateObjBy(@Param("objId") String objId, @Param("groupId") Long groupId);

    @Select("select id,obj_id,group_id,template_id from rel_warn_template_obj where FIND_IN_SET(obj_id,#{objIds}) and group_id=#{groupId}")
    @ResultType(RelWarnTemplateObj.class)
    List<RelWarnTemplateObj> selectRelWarnTemplateObjByObjIdsGroupId(@Param("objIds") String objIds, @Param("groupId") Long groupId);

    @Select("select id,name,content,group_id from sys_warn_template where id=#{id} and del_flag=0 limit 1")
    @ResultType(SysWarnTemplate.class)
    SysWarnTemplate selectWarnTemplateById(@Param("id") Long id);

    @Select("select id,receive_user_id,receive_phone,msg,checked,\n" +
            "    next_receive_ids,msg_type,check_time,warn_obj_id,\n" +
            "    group_id,warning_time from sys_warn_log where warn_obj_id=#{warnObjId}")
    @ResultType(SysWarnLog.class)
    List<SysWarnLog> selectWarnLogByWarnObjId(Long warnObjId);

    @Select("select id,receive_user_id,receive_phone,msg,checked,\n" +
            "    next_receive_ids,msg_type,check_time,warn_obj_id,\n" +
            "    group_id,warning_time from " +
            " (select * from sys_warn_log where " +
            " warn_obj_id is not null and " +
            " warning_time is not null and " +
            " FIND_IN_SET(warn_obj_id,#{warnObjIds})" +
            " HAVING 1 ORDER BY warning_time) " +
            " as a  GROUP BY a.warn_obj_id")
    @ResultType(SysWarnLog.class)
    List<SysWarnLog> selectNewsestWarnLogByObjWarnId(String warnObjIds);

}
