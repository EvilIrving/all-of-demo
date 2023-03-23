package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRvwlBaseReqVo;
import com.ygkj.river.vo.response.AttRvwlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRvwlBaseService {
    PageVO<AttRvwlBaseRespVo> loadPage(AttRvwlBaseReqVo vo);
}
