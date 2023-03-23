package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRvclBaseReqVo;
import com.ygkj.river.vo.response.AttRvclBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRvclBaseService {
    PageVO<AttRvclBaseRespVo> loadPage(AttRvclBaseReqVo vo);
}
