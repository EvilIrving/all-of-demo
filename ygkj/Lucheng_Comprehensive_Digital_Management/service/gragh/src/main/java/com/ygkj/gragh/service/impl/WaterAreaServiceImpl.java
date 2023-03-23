package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.SpaceWaterAreaMapper;
import com.ygkj.gragh.model.SpaceWaterArea;
import com.ygkj.gragh.service.WaterAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaterAreaServiceImpl implements WaterAreaService {

    @Autowired
    private SpaceWaterAreaMapper waterAreaMapper;

    @Override
    public List<SpaceWaterArea> waterAreas() {
        return waterAreaMapper.allWaterAreas();
    }
}
