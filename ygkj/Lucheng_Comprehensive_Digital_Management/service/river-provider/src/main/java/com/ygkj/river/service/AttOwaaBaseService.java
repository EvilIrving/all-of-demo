package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttOwaaBaseReqVo;
import com.ygkj.river.vo.response.AttOwaaBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttOwaaBaseService {
    PageVO<AttOwaaBaseRespVo> loadPage(AttOwaaBaseReqVo vo);
}
