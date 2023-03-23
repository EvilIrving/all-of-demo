package com.ygkj.visualization.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.visualization.service.RainDataSurfaceService;
import org.springframework.beans.factory.annotation.Autowired;

//@Service
public class RainDataSurfaceServiceImpl implements RainDataSurfaceService {

    private FiveKilometerRainDataManager rainDataManager;

    public RainDataSurfaceServiceImpl(@Autowired FiveKilometerRainDataManager rainDataManager) {
        this.rainDataManager = rainDataManager;
    }

    @Override
    public JSONArray get5kmCorrectRainData() {
        return rainDataManager.get5kmCorrectData();
    }
}
