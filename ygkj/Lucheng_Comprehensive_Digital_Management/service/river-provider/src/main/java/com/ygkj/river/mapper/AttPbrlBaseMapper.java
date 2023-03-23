package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttPbrlBaseReqVo;
import com.ygkj.river.vo.response.AttPbrlBaseRespVo;

import java.util.List;

public interface AttPbrlBaseMapper {
    List<AttPbrlBaseRespVo> loadPage(AttPbrlBaseReqVo vo);
}
