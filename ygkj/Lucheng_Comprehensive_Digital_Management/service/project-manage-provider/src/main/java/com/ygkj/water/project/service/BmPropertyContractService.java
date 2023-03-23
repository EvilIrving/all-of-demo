package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.BmPropertyContractReqVo;

public interface BmPropertyContractService {
    void insert(BmPropertyContractReqVo vo);

    void update(BmPropertyContractReqVo vo);

    Object load(BmPropertyContractReqVo vo);

    void delete(String id);
}
