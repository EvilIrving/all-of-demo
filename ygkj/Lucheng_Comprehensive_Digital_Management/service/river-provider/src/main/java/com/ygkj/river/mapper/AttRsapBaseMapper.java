package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRsapBaseReqVo;
import com.ygkj.river.vo.response.AttRsapBaseRespVo;

import java.util.List;

public interface AttRsapBaseMapper {
    List<AttRsapBaseRespVo> loadPage(AttRsapBaseReqVo vo);
}
