package com.ygkj.water.project.service.impl;

import com.ygkj.project.model.StWaterPlantR;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.project.mapper.WaterPlantMapper;
import com.ygkj.water.project.service.WaterPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterPlantServiceImpl implements WaterPlantService {
    @Autowired
    private WaterPlantMapper waterPlantMapper;

    @Override
    public void insertBatch(List<StWaterPlantR> list) {
        if (CollectionUtils.isNotBlank(list)){
            waterPlantMapper.insertBatch(list);
        }

    }
}
