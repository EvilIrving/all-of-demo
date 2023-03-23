package com.ygkj.project.service;

import com.ygkj.project.api.vo.res.RelGatestationResVo;
import com.ygkj.project.mapper.GateStationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateStationService {

    @Autowired
    private GateStationMapper gateStationMapper;

    public RelGatestationResVo selectByPrcd(String prcd) {
        return gateStationMapper.selectByPrcd(prcd);
    }
}
