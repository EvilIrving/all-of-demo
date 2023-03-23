package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.MtTaskNode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author lxl
 * @create 2022-04-21 17:33
 * @description
 */
public interface MtTaskNodeMapper extends BaseMapper<MtTaskNode> {

    /**
     * 判断是否存在于水管单位
     * @param userId
     * @return
     */
    @Select("select id from sys_user where dept_id in (select id from sys_dept_water_manage ) and id = #{userId} ")
    String judgeWaterManageUser(@Param("userId") String userId);
}
