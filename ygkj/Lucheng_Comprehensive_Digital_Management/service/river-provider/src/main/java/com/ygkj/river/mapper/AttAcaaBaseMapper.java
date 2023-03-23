package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttAcaaBaseReqVo;
import com.ygkj.river.vo.response.AttAcaaBaseRespVo;

import java.util.List;

public interface AttAcaaBaseMapper {
    List<AttAcaaBaseRespVo> loadPage(AttAcaaBaseReqVo vo);
}
