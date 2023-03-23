package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRsalBaseReqVo;
import com.ygkj.river.vo.response.AttRsalBaseRespVo;

import java.util.List;

public interface AttRsalBaseMapper {

    List<AttRsalBaseRespVo> loadPage(AttRsalBaseReqVo vo);

}
