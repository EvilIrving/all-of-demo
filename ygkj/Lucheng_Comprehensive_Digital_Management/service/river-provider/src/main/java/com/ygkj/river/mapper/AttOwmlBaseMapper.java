package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttOwmlBaseReqVo;
import com.ygkj.river.vo.response.AttOwmlBaseRespVo;

import java.util.List;

public interface AttOwmlBaseMapper {
    List<AttOwmlBaseRespVo> loadPage(AttOwmlBaseReqVo vo);
}
