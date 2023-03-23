package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttPdklBaseReqVo;
import com.ygkj.river.vo.response.AttPdklBaseRespVo;

import java.util.List;

public interface AttPdklBaseMapper {
    List<AttPdklBaseRespVo> loadPage(AttPdklBaseReqVo vo);
}
