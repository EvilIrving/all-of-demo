package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttPdklBaseReqVo;
import com.ygkj.river.vo.response.AttPdklBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttPdklBaseService {
    PageVO<AttPdklBaseRespVo> loadPage(AttPdklBaseReqVo vo);
}
