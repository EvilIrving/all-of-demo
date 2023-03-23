package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.OverseeEvaluation;

/**
 * @author wgf
 * @date 2022/6/10 11:23
 * @Description:
 */
public interface OverseeEvaluationService extends IService<OverseeEvaluation> {

    String getUserName(String userId);

    String getWaterDept(String deptId);
}
