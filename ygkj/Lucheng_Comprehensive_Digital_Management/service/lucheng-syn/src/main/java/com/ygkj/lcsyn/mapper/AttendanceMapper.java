package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.lcsyn.model.AttendanceDataBase;
import com.ygkj.lcsyn.model.AttendancePersonnelBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lxl
 * @create 2022-07-05 11:03
 * @description
 */
@DS("basic")
public interface AttendanceMapper{


    int batchReplacePersonnel(@Param("list") List<AttendancePersonnelBase> list);

    int batchReplaceDatas(@Param("list") List<AttendanceDataBase> dataBases);

    @Select("select create_time from attendance_data_base order by create_time desc limit 1 ")
    String findNewDateTime();

}
