package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttOwapBaseReqVo;
import com.ygkj.river.vo.response.AttOwapBaseRespVo;

import java.util.List;

public interface AttOwapBaseMapper {
    List<AttOwapBaseRespVo> loadPage(AttOwapBaseReqVo vo);
}
