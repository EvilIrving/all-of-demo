package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttPoppBaseReqVo;
import com.ygkj.river.vo.response.AttPoppBaseRespVo;

import java.util.List;

public interface AttPoppBaseMapper {
    List<AttPoppBaseRespVo> loadPage(AttPoppBaseReqVo vo);
}
