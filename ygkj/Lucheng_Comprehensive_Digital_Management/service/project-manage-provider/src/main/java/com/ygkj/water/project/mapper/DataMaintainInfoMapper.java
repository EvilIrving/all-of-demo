package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.DataMaintainInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface DataMaintainInfoMapper extends BaseMapper<DataMaintainInfo> {
    @Select("select id from sys_user where id = #{userId} and is_admin = 1")
    String selectAdmin(@Param("userId") String userId);
    @Select("select phone from sys_user where id = #{userId} ")
    String getPhone(@Param("userId")String userId);

}
