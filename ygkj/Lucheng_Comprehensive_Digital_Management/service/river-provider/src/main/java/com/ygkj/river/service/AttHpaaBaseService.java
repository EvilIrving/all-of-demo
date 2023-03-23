package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttHpaaBaseReqVo;
import com.ygkj.river.vo.response.AttHpaaBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttHpaaBaseService {
    PageVO<AttHpaaBaseRespVo> loadPage(AttHpaaBaseReqVo vo);
}
