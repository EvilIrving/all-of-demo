package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRvaaBaseReqVo;
import com.ygkj.river.vo.response.AttRvaaBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRvaaBaseService {
    PageVO<AttRvaaBaseRespVo> loadPage(AttRvaaBaseReqVo vo);
}
