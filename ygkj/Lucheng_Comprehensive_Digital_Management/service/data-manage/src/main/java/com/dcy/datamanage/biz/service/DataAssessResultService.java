package com.dcy.datamanage.biz.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.DataAssessResult;
import com.dcy.datamanage.biz.mapper.DataAssessResultMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataAssessResultService extends ServiceImpl<DataAssessResultMapper, DataAssessResult> {

    public void replaceAll(List<DataAssessResult> resultList) {
        baseMapper.replaceAll(resultList);
    }
}
