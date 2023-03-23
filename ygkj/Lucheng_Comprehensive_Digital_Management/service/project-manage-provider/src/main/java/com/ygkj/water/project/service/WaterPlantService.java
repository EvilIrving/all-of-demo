package com.ygkj.water.project.service;

import com.ygkj.project.model.StWaterPlantR;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface WaterPlantService {
    void insertBatch(List<StWaterPlantR> list);
}
