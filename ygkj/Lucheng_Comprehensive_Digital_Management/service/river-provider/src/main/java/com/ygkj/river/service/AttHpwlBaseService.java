package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttHpwlBaseReqVo;
import com.ygkj.river.vo.response.AttHpwlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttHpwlBaseService {
    PageVO<AttHpwlBaseRespVo> loadPage(AttHpwlBaseReqVo vo);
}
