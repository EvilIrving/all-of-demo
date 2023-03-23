package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.ConAcceptanceApplicationNodeReqVo;

public interface ConAcceptanceApplicationNodeService {
    void pass(ConAcceptanceApplicationNodeReqVo vo);

    void reject(ConAcceptanceApplicationNodeReqVo vo);

    Object loadNodes(ConAcceptanceApplicationNodeReqVo vo);

    void reminder(ConAcceptanceApplicationNodeReqVo vo);
}
