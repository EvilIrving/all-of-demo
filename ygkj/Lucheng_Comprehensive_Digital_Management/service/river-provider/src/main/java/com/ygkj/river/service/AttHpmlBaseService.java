package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttHpmlBaseReqVo;
import com.ygkj.river.vo.response.AttHpmlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttHpmlBaseService {
    PageVO<AttHpmlBaseRespVo> loadPage(AttHpmlBaseReqVo vo);
}
