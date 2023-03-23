package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttOwalBaseReqVo;
import com.ygkj.river.vo.response.AttOwalBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttOwalBaseService {
    PageVO<AttOwalBaseRespVo> loadPage(AttOwalBaseReqVo vo);
}
