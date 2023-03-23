package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttHpapBaseReqVo;
import com.ygkj.river.vo.response.AttHpapBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttHpapBaseService {
    PageVO<AttHpapBaseRespVo> loadPage(AttHpapBaseReqVo vo);
}
