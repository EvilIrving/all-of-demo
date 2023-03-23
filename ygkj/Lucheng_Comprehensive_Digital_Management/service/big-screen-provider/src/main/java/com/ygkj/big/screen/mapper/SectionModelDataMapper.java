package com.ygkj.big.screen.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.big.screen.model.SectionModelData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SectionModelDataMapper extends BaseMapper<SectionModelData> {

    List<JSONObject> getForecastWaterLevelData(@Param("id") String id, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
