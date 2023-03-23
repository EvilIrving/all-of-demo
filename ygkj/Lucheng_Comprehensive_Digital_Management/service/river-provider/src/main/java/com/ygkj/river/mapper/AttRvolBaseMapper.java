package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRvolBaseReqVo;
import com.ygkj.river.vo.response.AttRvolBaseRespVo;

import java.util.List;

public interface AttRvolBaseMapper {
    List<AttRvolBaseRespVo> loadPage(AttRvolBaseReqVo vo);
}
