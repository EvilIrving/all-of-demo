package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.PushModel;
import com.ygkj.lcsyn.model.SimulationData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@DS("business")
public interface DataPushQueryMapper {


    List<PushModel> queryPtsnData(@Param("stcd") String stcd,@Param("startTime") Date startTime);


    List<PushModel> queryRiverData(@Param("stcd") String stcd,@Param("startTime") Date startTime);


    List<PushModel> queryWasData(@Param("stcd") String stcd,@Param("startTime") Date startTime);

    String queryPointData(@Param("tableName") String tableName,@Param("pointCode") String pointCode);

    PushModel queryLastData(String tableName,String pointCode);
}
