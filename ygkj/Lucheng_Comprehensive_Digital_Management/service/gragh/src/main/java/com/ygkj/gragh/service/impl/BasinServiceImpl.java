package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.AttBasBaseMapper;
import com.ygkj.gragh.model.AttBasBase;
import com.ygkj.gragh.service.BasinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("basinServiceImpl")
public class BasinServiceImpl implements BasinService {

    @Autowired
    private AttBasBaseMapper basinMapper;

    @Override
    public List<AttBasBase> basinList() {
        return basinMapper.selectAll();
    }
}
