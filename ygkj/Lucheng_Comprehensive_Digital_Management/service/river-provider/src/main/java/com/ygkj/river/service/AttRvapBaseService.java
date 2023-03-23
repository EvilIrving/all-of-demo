package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRvapBaseReqVo;
import com.ygkj.river.vo.response.AttRvapBaseRespVo;
import com.ygkj.vo.PageVO;

public interface AttRvapBaseService {

    PageVO<AttRvapBaseRespVo> loadPage(AttRvapBaseReqVo vo);

}
