package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttOwwlBaseReqVo;
import com.ygkj.river.vo.response.AttOwwlBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttOwwlBaseService {
    PageVO<AttOwwlBaseRespVo> loadPage(AttOwwlBaseReqVo vo);
}
