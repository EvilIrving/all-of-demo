package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRswlBaseReqVo;
import com.ygkj.river.vo.response.AttRswlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRswlBaseService {
    PageVO<AttRswlBaseRespVo> loadPage(AttRswlBaseReqVo vo);
}
