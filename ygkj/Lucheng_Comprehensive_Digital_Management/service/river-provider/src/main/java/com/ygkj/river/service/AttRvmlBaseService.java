package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRvmlBaseReqVo;
import com.ygkj.river.vo.response.AttRvmlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRvmlBaseService {
    PageVO<AttRvmlBaseRespVo> loadPage(AttRvmlBaseReqVo vo);
}
