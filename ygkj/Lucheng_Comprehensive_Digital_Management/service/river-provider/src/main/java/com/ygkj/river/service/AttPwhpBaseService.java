package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttPwhpBaseReqVo;
import com.ygkj.river.vo.response.AttPwhpBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttPwhpBaseService {
    PageVO<AttPwhpBaseRespVo> loadPage(AttPwhpBaseReqVo vo);
}
