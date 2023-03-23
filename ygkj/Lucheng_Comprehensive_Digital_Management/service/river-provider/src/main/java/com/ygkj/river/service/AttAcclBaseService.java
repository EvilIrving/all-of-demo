package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttAcclBaseReqVo;
import com.ygkj.river.vo.response.AttAcclBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttAcclBaseService {
    PageVO<AttAcclBaseRespVo> loadPage(AttAcclBaseReqVo vo);
}
