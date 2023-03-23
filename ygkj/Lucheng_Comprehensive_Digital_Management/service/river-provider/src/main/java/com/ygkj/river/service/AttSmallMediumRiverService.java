package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttSmallMediumRiverReqVo;
import com.ygkj.river.vo.response.AttSmallMediumRiverRespVo;
import com.ygkj.vo.PageVO;

public interface AttSmallMediumRiverService {

    PageVO<AttSmallMediumRiverRespVo> loadPage(AttSmallMediumRiverReqVo vo);

}
