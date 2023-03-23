package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttAcmlBaseReqVo;
import com.ygkj.river.vo.response.AttAcmlBaseRespVo;

import java.util.List;

public interface AttAcmlBaseMapper {
    List<AttAcmlBaseRespVo> loadPage(AttAcmlBaseReqVo vo);
}
