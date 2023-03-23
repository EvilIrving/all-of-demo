package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRvolBaseReqVo;
import com.ygkj.river.vo.response.AttRvolBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRvolBaseService {

    PageVO<AttRvolBaseRespVo> loadPage(AttRvolBaseReqVo vo);

}
