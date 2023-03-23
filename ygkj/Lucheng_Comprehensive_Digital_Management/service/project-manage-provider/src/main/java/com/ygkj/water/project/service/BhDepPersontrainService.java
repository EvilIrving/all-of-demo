package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.BhDepPersontrainReqVo;

public interface BhDepPersontrainService {
    void insert(BhDepPersontrainReqVo vo);

    void update(BhDepPersontrainReqVo vo);

    Object load(BhDepPersontrainReqVo vo);

    void delete(String id);

}
