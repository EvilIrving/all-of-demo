package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttPslpBaseReqVo;
import com.ygkj.river.vo.response.AttPslpBaseRespVo;

import java.util.List;

public interface AttPslpBaseMapper {
    List<AttPslpBaseRespVo> loadPage(AttPslpBaseReqVo vo);
}
