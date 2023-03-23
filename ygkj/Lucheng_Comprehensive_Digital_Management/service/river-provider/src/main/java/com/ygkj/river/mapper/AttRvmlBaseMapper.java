package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRvmlBaseReqVo;
import com.ygkj.river.vo.response.AttRvmlBaseRespVo;

import java.util.List;

public interface AttRvmlBaseMapper {
    List<AttRvmlBaseRespVo> loadPage(AttRvmlBaseReqVo vo);
}
