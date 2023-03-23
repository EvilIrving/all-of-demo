package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttAcclBaseReqVo;
import com.ygkj.river.vo.response.AttAcclBaseRespVo;

import java.util.List;

public interface AttAcclBaseMapper {
    List<AttAcclBaseRespVo> loadPage(AttAcclBaseReqVo vo);
}
