package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRsaaBaseReqVo;
import com.ygkj.river.vo.response.AttRsaaBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRsaaBaseService {

    PageVO<AttRsaaBaseRespVo> loadPage(AttRsaaBaseReqVo vo);

}
