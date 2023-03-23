package com.ygkj.project.service;

import com.ygkj.project.api.vo.res.RelPumpResVo;
import com.ygkj.project.mapper.PumpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PumpService {

    @Autowired
    private PumpMapper pumpMapper;

    public RelPumpResVo selectByPrcd(String prcd) {
        return pumpMapper.selectByPrcd(prcd);
    }
}
