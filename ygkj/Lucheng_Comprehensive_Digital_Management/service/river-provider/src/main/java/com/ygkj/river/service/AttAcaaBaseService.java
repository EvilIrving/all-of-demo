package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttAcaaBaseReqVo;
import com.ygkj.river.vo.response.AttAcaaBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttAcaaBaseService {
    PageVO<AttAcaaBaseRespVo> loadPage(AttAcaaBaseReqVo vo);
}
