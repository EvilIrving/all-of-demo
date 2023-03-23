package com.ygkj.visualization.service.impl;

import com.ygkj.visualization.mapper.WindFarmMapper;
import com.ygkj.visualization.model.WindFarm;
import com.ygkj.visualization.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("windFarmService")
public class WindFarmServiceImpl implements SiteService {

    private WindFarmMapper windFarmMapper;

    public WindFarmServiceImpl(@Autowired WindFarmMapper windFarmMapper) {
        this.windFarmMapper = windFarmMapper;
    }

    @Override
    public List<Map<String, String>> siteTimeList() {
        return windFarmMapper.selectWithoutDetail();
    }

    @Override
    public WindFarm loadWindFarm(Integer id) {
        return windFarmMapper.load(id.intValue());
    }
}
