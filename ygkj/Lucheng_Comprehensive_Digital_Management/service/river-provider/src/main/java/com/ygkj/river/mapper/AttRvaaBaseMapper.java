package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRvaaBaseReqVo;
import com.ygkj.river.vo.response.AttRvaaBaseRespVo;

import java.util.List;

public interface AttRvaaBaseMapper {
    List<AttRvaaBaseRespVo> loadPage(AttRvaaBaseReqVo vo);
}
