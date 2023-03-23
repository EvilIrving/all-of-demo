package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttAcalBaseReqVo;
import com.ygkj.river.vo.response.AttAcalBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttAcalBaseService {
    PageVO<AttAcalBaseRespVo> loadPage(AttAcalBaseReqVo vo);
}
