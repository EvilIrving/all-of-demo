package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttHpalBaseReqVo;
import com.ygkj.river.vo.response.AttHpalBaseRespVo;

import java.util.List;

public interface AttHpalBaseMapper {
    List<AttHpalBaseRespVo> loadPage(AttHpalBaseReqVo vo);
}
