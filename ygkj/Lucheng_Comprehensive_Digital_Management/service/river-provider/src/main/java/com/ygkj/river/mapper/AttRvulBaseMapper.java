package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRvulBaseReqVo;
import com.ygkj.river.vo.response.AttRvulBaseRespVo;

import java.util.List;

public interface AttRvulBaseMapper {
    List<AttRvulBaseRespVo> loadPage(AttRvulBaseReqVo vo);
}
