package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.project.model.StWaterPlantR;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterPlantMapper {
    @DS("business")
    int insertBatch(List<StWaterPlantR> list);

}
