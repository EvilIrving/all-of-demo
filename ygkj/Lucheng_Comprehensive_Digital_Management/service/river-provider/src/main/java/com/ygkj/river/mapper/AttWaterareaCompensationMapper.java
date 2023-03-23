package com.ygkj.river.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.vo.response.AttWaterareaCompensationRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttWaterareaCompensationMapper {

    AttWaterareaCompensationRespVo statisticsReclaimedAreaWater(@Param("year") Integer year);

    List<Integer> calYear();

    List<JSONObject> statisticsReclaimedByYear(@Param("year") Integer year);

    List<AttWaterareaCompensationRespVo> waterAreaEachAdministrative(@Param("year") Integer year);
}
