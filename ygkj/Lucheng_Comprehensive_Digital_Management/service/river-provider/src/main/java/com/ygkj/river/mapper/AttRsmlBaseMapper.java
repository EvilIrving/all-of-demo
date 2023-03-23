package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRsmlBaseReqVo;
import com.ygkj.river.vo.response.AttRsmlBaseRespVo;

import java.util.List;

public interface AttRsmlBaseMapper {
    List<AttRsmlBaseRespVo> loadPage(AttRsmlBaseReqVo vo);
}
