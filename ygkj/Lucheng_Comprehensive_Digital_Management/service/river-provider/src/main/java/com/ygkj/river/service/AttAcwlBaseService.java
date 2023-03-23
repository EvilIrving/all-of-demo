package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttAcwlBaseReqVo;
import com.ygkj.river.vo.response.AttAcwlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttAcwlBaseService {
    PageVO<AttAcwlBaseRespVo> loadPage(AttAcwlBaseReqVo vo);
}
