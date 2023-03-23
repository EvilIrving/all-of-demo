package com.ygkj.water.project.mapper;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.WarningUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wgf
 * @date 2021/10/21 17:55
 * @Description:
 */
public interface WarningUserMapper extends BaseMapper<WarningUser> {

    @Select("select concat( 'lcslj', wu.`id`) as `id`, concat(wu.`name`,'-',dc.deptName) as `name`, wu.phone as phone from warning_user wu inner join " +
            "dept_custom  dc on wu.deptId = dc.id")
    List<JSONObject> addressBookList(@Param("key") String key);

    @Select("<script>select t1.*,t2.id deptId,t2.deptName from warning_user t1 left join dept_custom t2 on t1.deptId=t2.id " +
            "<if test='null!=name'>where name like concat('%',#{name},'%')</if></script>")
    List<JSONObject> userList(String name);
}
