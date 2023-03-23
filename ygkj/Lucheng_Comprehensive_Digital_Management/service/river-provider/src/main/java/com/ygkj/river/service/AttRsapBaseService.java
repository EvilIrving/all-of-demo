package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRsapBaseReqVo;
import com.ygkj.river.vo.response.AttRsapBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRsapBaseService {

    PageVO<AttRsapBaseRespVo> loadPage(AttRsapBaseReqVo vo);

}
