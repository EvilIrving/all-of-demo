package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttAcapBaseReqVo;
import com.ygkj.river.vo.response.AttAcapBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttAcapBaseService {
    PageVO<AttAcapBaseRespVo> loadPage(AttAcapBaseReqVo vo);
}
