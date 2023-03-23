package com.ygkj.visualization.service.impl;

import com.ygkj.visualization.mapper.WaveFieldMapper;
import com.ygkj.visualization.model.WindFarm;
import com.ygkj.visualization.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("waveFieldService")
public class WaveFieldServiceImpl implements SiteService {

    private WaveFieldMapper waveFieldMapper;

    public WaveFieldServiceImpl(@Autowired WaveFieldMapper waveFieldMapper) {
        this.waveFieldMapper = waveFieldMapper;
    }

    @Override
    public List<Map<String, String>> siteTimeList() {
        return waveFieldMapper.selectWithoutDetail();
    }

    @Override
    public WindFarm loadWindFarm(Integer id) {
        return waveFieldMapper.load(id.intValue());
    }
}
