package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttPslpBaseReqVo;
import com.ygkj.river.vo.response.AttPslpBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttPslpBaseService {
    PageVO<AttPslpBaseRespVo> loadPage(AttPslpBaseReqVo vo);
}
