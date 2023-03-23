package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttPbrlBaseReqVo;
import com.ygkj.river.vo.response.AttPbrlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttPbrlBaseService {
    PageVO<AttPbrlBaseRespVo> loadPage(AttPbrlBaseReqVo vo);
}
