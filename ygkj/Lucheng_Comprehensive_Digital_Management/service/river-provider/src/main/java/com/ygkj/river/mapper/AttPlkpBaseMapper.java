package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttPlkpBaseReqVo;
import com.ygkj.river.vo.response.AttPlkpBaseRespVo;

import java.util.List;

public interface AttPlkpBaseMapper {
    List<AttPlkpBaseRespVo> loadPage(AttPlkpBaseReqVo vo);
}
