package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttPpspBaseReqVo;
import com.ygkj.river.vo.response.AttPpspBaseRespVo;

import java.util.List;

public interface AttPpspBaseMapper {
    List<AttPpspBaseRespVo> loadPage(AttPpspBaseReqVo vo);
}
