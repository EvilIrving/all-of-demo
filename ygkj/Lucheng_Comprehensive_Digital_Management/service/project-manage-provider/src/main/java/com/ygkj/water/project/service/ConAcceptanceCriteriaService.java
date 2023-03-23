package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.ConAcceptanceCriteriaReqVo;

public interface ConAcceptanceCriteriaService {
    void insert(ConAcceptanceCriteriaReqVo vo);

    void update(ConAcceptanceCriteriaReqVo vo);

    Object load(ConAcceptanceCriteriaReqVo vo);

    void delete(String id);
}
