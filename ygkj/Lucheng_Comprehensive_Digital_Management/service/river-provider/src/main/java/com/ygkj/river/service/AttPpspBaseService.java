package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttPpspBaseReqVo;
import com.ygkj.river.vo.response.AttPpspBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttPpspBaseService {
    PageVO<AttPpspBaseRespVo> loadPage(AttPpspBaseReqVo vo);
}
