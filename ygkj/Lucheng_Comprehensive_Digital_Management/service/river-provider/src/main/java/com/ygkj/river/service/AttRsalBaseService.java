package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRsalBaseReqVo;
import com.ygkj.river.vo.response.AttRsalBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRsalBaseService {
    PageVO<AttRsalBaseRespVo> loadPage(AttRsalBaseReqVo vo);
}
