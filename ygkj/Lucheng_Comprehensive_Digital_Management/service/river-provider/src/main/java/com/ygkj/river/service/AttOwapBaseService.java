package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttOwapBaseReqVo;
import com.ygkj.river.vo.response.AttOwapBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttOwapBaseService {
    PageVO<AttOwapBaseRespVo> loadPage(AttOwapBaseReqVo vo);
}
