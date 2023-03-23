package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttSmallMediumRiverReqVo;
import com.ygkj.river.vo.response.AttSmallMediumRiverRespVo;

import java.util.List;

public interface AttSmallMediumRiverMapper {
    List<AttSmallMediumRiverRespVo> loadPage(AttSmallMediumRiverReqVo vo);
}
