package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRsaaBaseReqVo;
import com.ygkj.river.vo.response.AttRsaaBaseRespVo;

import java.util.List;

public interface AttRsaaBaseMapper {
    List<AttRsaaBaseRespVo> loadPage(AttRsaaBaseReqVo vo);
}
