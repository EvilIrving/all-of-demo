package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttAcmlBaseReqVo;
import com.ygkj.river.vo.response.AttAcmlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttAcmlBaseService {
    PageVO<AttAcmlBaseRespVo> loadPage(AttAcmlBaseReqVo vo);
}
