package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.BhLgLmrDisDomGsReqVo;

public interface BhLgLmrDisDomGsService {

    void saveOrUpdateGate(BhLgLmrDisDomGsReqVo vo);

    int delete(String id);

    Object load(String id,String soId,String actlOpenTm);
}
