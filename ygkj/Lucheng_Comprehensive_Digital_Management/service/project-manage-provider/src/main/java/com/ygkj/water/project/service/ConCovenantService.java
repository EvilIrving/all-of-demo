package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.ConCovenantReqVo;

public interface ConCovenantService {
    void insert(ConCovenantReqVo vo);

    void update(ConCovenantReqVo vo);

    Object load(ConCovenantReqVo vo);

    void delete(String id);
}
