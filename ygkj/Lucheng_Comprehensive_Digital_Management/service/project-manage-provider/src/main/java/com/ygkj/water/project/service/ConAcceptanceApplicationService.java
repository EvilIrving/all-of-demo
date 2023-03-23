package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.ConAcceptanceApplicationReqVo;

public interface ConAcceptanceApplicationService {
    void insert(ConAcceptanceApplicationReqVo vo);

    void update(ConAcceptanceApplicationReqVo vo);

    Object loadPage(ConAcceptanceApplicationReqVo vo);

    void delete(String id);

    Object count(ConAcceptanceApplicationReqVo vo);

    Object appCount();
}
