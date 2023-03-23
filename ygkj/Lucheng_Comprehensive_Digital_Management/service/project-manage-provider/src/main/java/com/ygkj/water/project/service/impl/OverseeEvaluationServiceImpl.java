package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.OverseeEvaluation;
import com.ygkj.water.project.mapper.OverseeEvaluationMapper;
import com.ygkj.water.project.service.OverseeEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wgf
 * @date 2022/6/10 11:23
 * @Description:
 */
@Service
public class OverseeEvaluationServiceImpl extends ServiceImpl<OverseeEvaluationMapper, OverseeEvaluation> implements OverseeEvaluationService {

    @Autowired
    private OverseeEvaluationMapper overseeEvaluationMapper;

    @Override
    public String getUserName(String userId) {
        return overseeEvaluationMapper.getUserName(userId);
    }

    @Override
    public String getWaterDept(String deptId) {
        return overseeEvaluationMapper.getWaterDept(deptId);
    }
}
