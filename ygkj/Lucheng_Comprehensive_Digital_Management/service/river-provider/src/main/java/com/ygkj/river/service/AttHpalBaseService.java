package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttHpalBaseReqVo;
import com.ygkj.river.vo.response.AttHpalBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttHpalBaseService {

    PageVO<AttHpalBaseRespVo> loadPage(AttHpalBaseReqVo vo);

}
