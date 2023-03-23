package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttPwhpBaseReqVo;
import com.ygkj.river.vo.response.AttPwhpBaseRespVo;

import java.util.List;

public interface AttPwhpBaseMapper {
    List<AttPwhpBaseRespVo> loadPage(AttPwhpBaseReqVo vo);
}
