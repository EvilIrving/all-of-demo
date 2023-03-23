package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRswlBaseReqVo;
import com.ygkj.river.vo.response.AttRswlBaseRespVo;

import java.util.List;

public interface AttRswlBaseMapper {
    List<AttRswlBaseRespVo> loadPage(AttRswlBaseReqVo vo);
}
