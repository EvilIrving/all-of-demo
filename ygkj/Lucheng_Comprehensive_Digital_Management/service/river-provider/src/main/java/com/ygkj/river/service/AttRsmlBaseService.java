package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRsmlBaseReqVo;
import com.ygkj.river.vo.response.AttRsmlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRsmlBaseService {
    PageVO<AttRsmlBaseRespVo> loadPage(AttRsmlBaseReqVo vo);
}
