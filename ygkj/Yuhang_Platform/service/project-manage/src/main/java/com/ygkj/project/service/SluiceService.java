package com.ygkj.project.service;

import com.ygkj.project.api.vo.res.RelSluiceResVo;
import com.ygkj.project.mapper.SluiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SluiceService {

    @Autowired
    private SluiceMapper sluiceMapper;

    public RelSluiceResVo selectByPrcd(String prcd) {
        return sluiceMapper.selectByPrcd(prcd);
    }
}
