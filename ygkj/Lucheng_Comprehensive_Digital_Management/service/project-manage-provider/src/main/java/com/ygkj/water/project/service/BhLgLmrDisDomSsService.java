package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.BhLgLmrDisDomSsReqVo;

public interface BhLgLmrDisDomSsService {
    void saveOrUpdateGate(BhLgLmrDisDomSsReqVo vo);

    int delete(String id);

    Object load(String id,String soId,String actlOpenTm);
}
