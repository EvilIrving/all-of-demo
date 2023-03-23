package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRvulBaseReqVo;
import com.ygkj.river.vo.response.AttRvulBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRvulBaseService {
    PageVO<AttRvulBaseRespVo> loadPage(AttRvulBaseReqVo vo);
}
