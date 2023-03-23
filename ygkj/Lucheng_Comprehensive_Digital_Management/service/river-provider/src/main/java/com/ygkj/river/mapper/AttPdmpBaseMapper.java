package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttPdmpBaseReqVo;
import com.ygkj.river.vo.response.AttPdmpBaseRespVo;

import java.util.List;

public interface AttPdmpBaseMapper {
    List<AttPdmpBaseRespVo> loadPage(AttPdmpBaseReqVo vo);
}
