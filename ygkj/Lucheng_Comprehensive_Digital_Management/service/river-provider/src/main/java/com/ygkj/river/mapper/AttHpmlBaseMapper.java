package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttHpmlBaseReqVo;
import com.ygkj.river.vo.response.AttHpmlBaseRespVo;

import java.util.List;

public interface AttHpmlBaseMapper {
    List<AttHpmlBaseRespVo> loadPage(AttHpmlBaseReqVo vo);
}
