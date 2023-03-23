package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttPoppBaseReqVo;
import com.ygkj.river.vo.response.AttPoppBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttPoppBaseService {
    PageVO<AttPoppBaseRespVo> loadPage(AttPoppBaseReqVo vo);
}
