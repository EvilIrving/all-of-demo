package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.PushModel;
import com.ygkj.lcsyn.model.SimulationPoint;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@DS("push")
public interface DataPushTemporaryMapper {


    @Insert("insert into st_river_r_06731 (id,assumedTime,actualTime,dataValue) values (#{obj.id},#{obj.assumedTime},#{obj.actualTime},#{obj.dataValue}); ")
    void addRiverData(@Param("obj")PushModel pushModel);

    @Select("select *  from simulation_point where del_flag = '0' and point_basin is not null and point_basin != ''  ")
    List<SimulationPoint> queryBasinPoints();

    void replace(@Param("id")String id,@Param("basin") String basin, @Param("actualTime") String actualTime, @Param("dataValue") double avgDrp);

    void addBasin1(@Param("id")String id, @Param("actualTime") Date actualTime, @Param("dataValue") double avgDrp);

    void addBasin2(@Param("id")String id, @Param("actualTime") Date actualTime, @Param("dataValue") double avgDrp);

    @Select("select * from st_river_r_06731 where actualTime = (select top 1 actualTime from st_river_r_06731 order by actualTime desc ) ")
    PushModel queryMaxRiverData();

    @Insert("insert into st_river_r_01653 (id,assumedTime,actualTime,dataValue) values (#{obj.id},#{obj.assumedTime},#{obj.actualTime},#{obj.dataValue}); ")
    void addWasData(@Param("obj") PushModel data);
}
