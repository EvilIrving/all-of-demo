package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttPlkpBaseReqVo;
import com.ygkj.river.vo.response.AttPlkpBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttPlkpBaseService {
    PageVO<AttPlkpBaseRespVo> loadPage(AttPlkpBaseReqVo vo);
}
