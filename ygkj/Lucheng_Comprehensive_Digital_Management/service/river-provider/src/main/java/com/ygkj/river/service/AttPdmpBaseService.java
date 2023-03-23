package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttPdmpBaseReqVo;
import com.ygkj.river.vo.response.AttPdmpBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttPdmpBaseService {
    PageVO<AttPdmpBaseRespVo> loadPage(AttPdmpBaseReqVo vo);
}
