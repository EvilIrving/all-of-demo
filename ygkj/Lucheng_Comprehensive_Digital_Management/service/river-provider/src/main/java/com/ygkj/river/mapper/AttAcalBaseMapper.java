package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttAcalBaseReqVo;
import com.ygkj.river.vo.response.AttAcalBaseRespVo;

import java.util.List;

public interface AttAcalBaseMapper {
    List<AttAcalBaseRespVo> loadPage(AttAcalBaseReqVo vo);
}
