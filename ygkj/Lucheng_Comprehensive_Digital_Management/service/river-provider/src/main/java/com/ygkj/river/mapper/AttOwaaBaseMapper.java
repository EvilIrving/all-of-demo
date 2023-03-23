package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttOwaaBaseReqVo;
import com.ygkj.river.vo.response.AttOwaaBaseRespVo;

import java.util.List;

public interface AttOwaaBaseMapper {
    List<AttOwaaBaseRespVo> loadPage(AttOwaaBaseReqVo vo);
}
