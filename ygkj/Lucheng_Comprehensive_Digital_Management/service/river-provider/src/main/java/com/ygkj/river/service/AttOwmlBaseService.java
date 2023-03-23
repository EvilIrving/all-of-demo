package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttOwmlBaseReqVo;
import com.ygkj.river.vo.response.AttOwmlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttOwmlBaseService {
    PageVO<AttOwmlBaseRespVo> loadPage(AttOwmlBaseReqVo vo);
}
